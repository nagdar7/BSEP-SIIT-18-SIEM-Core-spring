package com.siemcore.web.rest;

import com.siemcore.SiemCoreApp;

import com.siemcore.domain.Rule;
import com.siemcore.repository.RuleRepository;
import com.siemcore.service.RuleService;
import com.siemcore.repository.search.RuleSearchRepository;
import com.siemcore.service.dto.RuleDTO;
import com.siemcore.service.mapper.RuleMapper;
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
 * Test class for the RuleResource REST controller.
 *
 * @see RuleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCoreApp.class)
public class RuleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RULE = "AAAAAAAAAA";
    private static final String UPDATED_RULE = "BBBBBBBBBB";

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleSearchRepository ruleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restRuleMockMvc;

    private Rule rule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RuleResource ruleResource = new RuleResource(ruleService);
        this.restRuleMockMvc = MockMvcBuilders.standaloneSetup(ruleResource)
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
    public static Rule createEntity() {
        Rule rule = new Rule()
            .name(DEFAULT_NAME)
            .rule(DEFAULT_RULE);
        return rule;
    }

    @Before
    public void initTest() {
        ruleRepository.deleteAll();
        ruleSearchRepository.deleteAll();
        rule = createEntity();
    }

    @Test
    public void createRule() throws Exception {
        int databaseSizeBeforeCreate = ruleRepository.findAll().size();

        // Create the Rule
        RuleDTO ruleDTO = ruleMapper.toDto(rule);
        restRuleMockMvc.perform(post("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleDTO)))
            .andExpect(status().isCreated());

        // Validate the Rule in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeCreate + 1);
        Rule testRule = ruleList.get(ruleList.size() - 1);
        assertThat(testRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRule.getRule()).isEqualTo(DEFAULT_RULE);

        // Validate the Rule in Elasticsearch
        Rule ruleEs = ruleSearchRepository.findOne(testRule.getId());
        assertThat(ruleEs).isEqualToIgnoringGivenFields(testRule);
    }

    @Test
    public void createRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ruleRepository.findAll().size();

        // Create the Rule with an existing ID
        rule.setId("existing_id");
        RuleDTO ruleDTO = ruleMapper.toDto(rule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleMockMvc.perform(post("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rule in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllRules() throws Exception {
        // Initialize the database
        ruleRepository.save(rule);

        // Get all the ruleList
        restRuleMockMvc.perform(get("/api/rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rule.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rule").value(hasItem(DEFAULT_RULE.toString())));
    }

    @Test
    public void getRule() throws Exception {
        // Initialize the database
        ruleRepository.save(rule);

        // Get the rule
        restRuleMockMvc.perform(get("/api/rules/{id}", rule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rule.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.rule").value(DEFAULT_RULE.toString()));
    }

    @Test
    public void getNonExistingRule() throws Exception {
        // Get the rule
        restRuleMockMvc.perform(get("/api/rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRule() throws Exception {
        // Initialize the database
        ruleRepository.save(rule);
        ruleSearchRepository.save(rule);
        int databaseSizeBeforeUpdate = ruleRepository.findAll().size();

        // Update the rule
        Rule updatedRule = ruleRepository.findOne(rule.getId());
        updatedRule
            .name(UPDATED_NAME)
            .rule(UPDATED_RULE);
        RuleDTO ruleDTO = ruleMapper.toDto(updatedRule);

        restRuleMockMvc.perform(put("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleDTO)))
            .andExpect(status().isOk());

        // Validate the Rule in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeUpdate);
        Rule testRule = ruleList.get(ruleList.size() - 1);
        assertThat(testRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRule.getRule()).isEqualTo(UPDATED_RULE);

        // Validate the Rule in Elasticsearch
        Rule ruleEs = ruleSearchRepository.findOne(testRule.getId());
        assertThat(ruleEs).isEqualToIgnoringGivenFields(testRule);
    }

    @Test
    public void updateNonExistingRule() throws Exception {
        int databaseSizeBeforeUpdate = ruleRepository.findAll().size();

        // Create the Rule
        RuleDTO ruleDTO = ruleMapper.toDto(rule);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRuleMockMvc.perform(put("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleDTO)))
            .andExpect(status().isCreated());

        // Validate the Rule in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteRule() throws Exception {
        // Initialize the database
        ruleRepository.save(rule);
        ruleSearchRepository.save(rule);
        int databaseSizeBeforeDelete = ruleRepository.findAll().size();

        // Get the rule
        restRuleMockMvc.perform(delete("/api/rules/{id}", rule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ruleExistsInEs = ruleSearchRepository.exists(rule.getId());
        assertThat(ruleExistsInEs).isFalse();

        // Validate the database is empty
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchRule() throws Exception {
        // Initialize the database
        ruleRepository.save(rule);
        ruleSearchRepository.save(rule);

        // Search the rule
        restRuleMockMvc.perform(get("/api/_search/rules?query=id:" + rule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rule.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rule").value(hasItem(DEFAULT_RULE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rule.class);
        Rule rule1 = new Rule();
        rule1.setId("id1");
        Rule rule2 = new Rule();
        rule2.setId(rule1.getId());
        assertThat(rule1).isEqualTo(rule2);
        rule2.setId("id2");
        assertThat(rule1).isNotEqualTo(rule2);
        rule1.setId(null);
        assertThat(rule1).isNotEqualTo(rule2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleDTO.class);
        RuleDTO ruleDTO1 = new RuleDTO();
        ruleDTO1.setId("id1");
        RuleDTO ruleDTO2 = new RuleDTO();
        assertThat(ruleDTO1).isNotEqualTo(ruleDTO2);
        ruleDTO2.setId(ruleDTO1.getId());
        assertThat(ruleDTO1).isEqualTo(ruleDTO2);
        ruleDTO2.setId("id2");
        assertThat(ruleDTO1).isNotEqualTo(ruleDTO2);
        ruleDTO1.setId(null);
        assertThat(ruleDTO1).isNotEqualTo(ruleDTO2);
    }
}
