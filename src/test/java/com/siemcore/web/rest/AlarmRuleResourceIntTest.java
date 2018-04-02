package com.siemcore.web.rest;

import com.siemcore.SiemCoreApp;

import com.siemcore.domain.AlarmRule;
import com.siemcore.repository.AlarmRuleRepository;
import com.siemcore.service.AlarmRuleService;
import com.siemcore.repository.search.AlarmRuleSearchRepository;
import com.siemcore.service.dto.AlarmRuleDTO;
import com.siemcore.service.mapper.AlarmRuleMapper;
import com.siemcore.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.siemcore.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AlarmRuleResource REST controller.
 *
 * @see AlarmRuleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCoreApp.class)
public class AlarmRuleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RULE = "AAAAAAAAAA";
    private static final String UPDATED_RULE = "BBBBBBBBBB";

    @Autowired
    private AlarmRuleRepository alarmRuleRepository;

    @Autowired
    private AlarmRuleMapper alarmRuleMapper;

    @Autowired
    private AlarmRuleService alarmRuleService;

    @Autowired
    private AlarmRuleSearchRepository alarmRuleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAlarmRuleMockMvc;

    private AlarmRule alarmRule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlarmRuleResource alarmRuleResource = new AlarmRuleResource(alarmRuleService);
        this.restAlarmRuleMockMvc = MockMvcBuilders.standaloneSetup(alarmRuleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlarmRule createEntity() {
        AlarmRule alarmRule = new AlarmRule()
            .name(DEFAULT_NAME)
            .rule(DEFAULT_RULE);
        return alarmRule;
    }

    @Before
    public void initTest() {
        alarmRuleRepository.deleteAll();
        alarmRuleSearchRepository.deleteAll();
        alarmRule = createEntity();
    }

    @Test
    public void createAlarmRule() throws Exception {
        int databaseSizeBeforeCreate = alarmRuleRepository.findAll().size();

        // Create the AlarmRule
        AlarmRuleDTO alarmRuleDTO = alarmRuleMapper.toDto(alarmRule);
        restAlarmRuleMockMvc.perform(post("/api/alarm-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmRuleDTO)))
            .andExpect(status().isCreated());

        // Validate the AlarmRule in the database
        List<AlarmRule> alarmRuleList = alarmRuleRepository.findAll();
        assertThat(alarmRuleList).hasSize(databaseSizeBeforeCreate + 1);
        AlarmRule testAlarmRule = alarmRuleList.get(alarmRuleList.size() - 1);
        assertThat(testAlarmRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlarmRule.getRule()).isEqualTo(DEFAULT_RULE);

        // Validate the AlarmRule in Elasticsearch
        AlarmRule alarmRuleEs = alarmRuleSearchRepository.findOne(testAlarmRule.getId());
        assertThat(alarmRuleEs).isEqualToIgnoringGivenFields(testAlarmRule);
    }

    @Test
    public void createAlarmRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alarmRuleRepository.findAll().size();

        // Create the AlarmRule with an existing ID
        alarmRule.setId("existing_id");
        AlarmRuleDTO alarmRuleDTO = alarmRuleMapper.toDto(alarmRule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmRuleMockMvc.perform(post("/api/alarm-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlarmRule in the database
        List<AlarmRule> alarmRuleList = alarmRuleRepository.findAll();
        assertThat(alarmRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllAlarmRules() throws Exception {
        // Initialize the database
        alarmRuleRepository.save(alarmRule);

        // Get all the alarmRuleList
        restAlarmRuleMockMvc.perform(get("/api/alarm-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmRule.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rule").value(hasItem(DEFAULT_RULE.toString())));
    }

    @Test
    public void getAlarmRule() throws Exception {
        // Initialize the database
        alarmRuleRepository.save(alarmRule);

        // Get the alarmRule
        restAlarmRuleMockMvc.perform(get("/api/alarm-rules/{id}", alarmRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alarmRule.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.rule").value(DEFAULT_RULE.toString()));
    }

    @Test
    public void getNonExistingAlarmRule() throws Exception {
        // Get the alarmRule
        restAlarmRuleMockMvc.perform(get("/api/alarm-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAlarmRule() throws Exception {
        // Initialize the database
        alarmRuleRepository.save(alarmRule);
        alarmRuleSearchRepository.save(alarmRule);
        int databaseSizeBeforeUpdate = alarmRuleRepository.findAll().size();

        // Update the alarmRule
        AlarmRule updatedAlarmRule = alarmRuleRepository.findOne(alarmRule.getId());
        updatedAlarmRule
            .name(UPDATED_NAME)
            .rule(UPDATED_RULE);
        AlarmRuleDTO alarmRuleDTO = alarmRuleMapper.toDto(updatedAlarmRule);

        restAlarmRuleMockMvc.perform(put("/api/alarm-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmRuleDTO)))
            .andExpect(status().isOk());

        // Validate the AlarmRule in the database
        List<AlarmRule> alarmRuleList = alarmRuleRepository.findAll();
        assertThat(alarmRuleList).hasSize(databaseSizeBeforeUpdate);
        AlarmRule testAlarmRule = alarmRuleList.get(alarmRuleList.size() - 1);
        assertThat(testAlarmRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlarmRule.getRule()).isEqualTo(UPDATED_RULE);

        // Validate the AlarmRule in Elasticsearch
        AlarmRule alarmRuleEs = alarmRuleSearchRepository.findOne(testAlarmRule.getId());
        assertThat(alarmRuleEs).isEqualToIgnoringGivenFields(testAlarmRule);
    }

    @Test
    public void updateNonExistingAlarmRule() throws Exception {
        int databaseSizeBeforeUpdate = alarmRuleRepository.findAll().size();

        // Create the AlarmRule
        AlarmRuleDTO alarmRuleDTO = alarmRuleMapper.toDto(alarmRule);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAlarmRuleMockMvc.perform(put("/api/alarm-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmRuleDTO)))
            .andExpect(status().isCreated());

        // Validate the AlarmRule in the database
        List<AlarmRule> alarmRuleList = alarmRuleRepository.findAll();
        assertThat(alarmRuleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteAlarmRule() throws Exception {
        // Initialize the database
        alarmRuleRepository.save(alarmRule);
        alarmRuleSearchRepository.save(alarmRule);
        int databaseSizeBeforeDelete = alarmRuleRepository.findAll().size();

        // Get the alarmRule
        restAlarmRuleMockMvc.perform(delete("/api/alarm-rules/{id}", alarmRule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean alarmRuleExistsInEs = alarmRuleSearchRepository.exists(alarmRule.getId());
        assertThat(alarmRuleExistsInEs).isFalse();

        // Validate the database is empty
        List<AlarmRule> alarmRuleList = alarmRuleRepository.findAll();
        assertThat(alarmRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchAlarmRule() throws Exception {
        // Initialize the database
        alarmRuleRepository.save(alarmRule);
        alarmRuleSearchRepository.save(alarmRule);

        // Search the alarmRule
        restAlarmRuleMockMvc.perform(get("/api/_search/alarm-rules?query=id:" + alarmRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmRule.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rule").value(hasItem(DEFAULT_RULE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmRule.class);
        AlarmRule alarmRule1 = new AlarmRule();
        alarmRule1.setId("id1");
        AlarmRule alarmRule2 = new AlarmRule();
        alarmRule2.setId(alarmRule1.getId());
        assertThat(alarmRule1).isEqualTo(alarmRule2);
        alarmRule2.setId("id2");
        assertThat(alarmRule1).isNotEqualTo(alarmRule2);
        alarmRule1.setId(null);
        assertThat(alarmRule1).isNotEqualTo(alarmRule2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmRuleDTO.class);
        AlarmRuleDTO alarmRuleDTO1 = new AlarmRuleDTO();
        alarmRuleDTO1.setId("id1");
        AlarmRuleDTO alarmRuleDTO2 = new AlarmRuleDTO();
        assertThat(alarmRuleDTO1).isNotEqualTo(alarmRuleDTO2);
        alarmRuleDTO2.setId(alarmRuleDTO1.getId());
        assertThat(alarmRuleDTO1).isEqualTo(alarmRuleDTO2);
        alarmRuleDTO2.setId("id2");
        assertThat(alarmRuleDTO1).isNotEqualTo(alarmRuleDTO2);
        alarmRuleDTO1.setId(null);
        assertThat(alarmRuleDTO1).isNotEqualTo(alarmRuleDTO2);
    }
}
