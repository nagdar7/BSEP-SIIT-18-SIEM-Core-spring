package com.siemcore.web.rest;

import com.siemcore.SiemCoreApp;

import com.siemcore.domain.AlarmHistory;
import com.siemcore.repository.AlarmHistoryRepository;
import com.siemcore.service.AlarmHistoryService;
import com.siemcore.repository.search.AlarmHistorySearchRepository;
import com.siemcore.service.dto.AlarmHistoryDTO;
import com.siemcore.service.mapper.AlarmHistoryMapper;
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

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.siemcore.web.rest.TestUtil.sameInstant;
import static com.siemcore.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AlarmHistoryResource REST controller.
 *
 * @see AlarmHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCoreApp.class)
public class AlarmHistoryResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AlarmHistoryRepository alarmHistoryRepository;

    @Autowired
    private AlarmHistoryMapper alarmHistoryMapper;

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    @Autowired
    private AlarmHistorySearchRepository alarmHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAlarmHistoryMockMvc;

    private AlarmHistory alarmHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlarmHistoryResource alarmHistoryResource = new AlarmHistoryResource(alarmHistoryService);
        this.restAlarmHistoryMockMvc = MockMvcBuilders.standaloneSetup(alarmHistoryResource)
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
    public static AlarmHistory createEntity() {
        AlarmHistory alarmHistory = new AlarmHistory()
            .date(DEFAULT_DATE);
        return alarmHistory;
    }

    @Before
    public void initTest() {
        alarmHistoryRepository.deleteAll();
        alarmHistorySearchRepository.deleteAll();
        alarmHistory = createEntity();
    }

    @Test
    public void createAlarmHistory() throws Exception {
        int databaseSizeBeforeCreate = alarmHistoryRepository.findAll().size();

        // Create the AlarmHistory
        AlarmHistoryDTO alarmHistoryDTO = alarmHistoryMapper.toDto(alarmHistory);
        restAlarmHistoryMockMvc.perform(post("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        AlarmHistory testAlarmHistory = alarmHistoryList.get(alarmHistoryList.size() - 1);
        assertThat(testAlarmHistory.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the AlarmHistory in Elasticsearch
        AlarmHistory alarmHistoryEs = alarmHistorySearchRepository.findOne(testAlarmHistory.getId());
        assertThat(testAlarmHistory.getDate()).isEqualTo(testAlarmHistory.getDate());
        assertThat(alarmHistoryEs).isEqualToIgnoringGivenFields(testAlarmHistory, "date");
    }

    @Test
    public void createAlarmHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alarmHistoryRepository.findAll().size();

        // Create the AlarmHistory with an existing ID
        alarmHistory.setId("existing_id");
        AlarmHistoryDTO alarmHistoryDTO = alarmHistoryMapper.toDto(alarmHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmHistoryMockMvc.perform(post("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllAlarmHistories() throws Exception {
        // Initialize the database
        alarmHistoryRepository.save(alarmHistory);

        // Get all the alarmHistoryList
        restAlarmHistoryMockMvc.perform(get("/api/alarm-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmHistory.getId())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    public void getAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.save(alarmHistory);

        // Get the alarmHistory
        restAlarmHistoryMockMvc.perform(get("/api/alarm-histories/{id}", alarmHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alarmHistory.getId()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    public void getNonExistingAlarmHistory() throws Exception {
        // Get the alarmHistory
        restAlarmHistoryMockMvc.perform(get("/api/alarm-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.save(alarmHistory);
        alarmHistorySearchRepository.save(alarmHistory);
        int databaseSizeBeforeUpdate = alarmHistoryRepository.findAll().size();

        // Update the alarmHistory
        AlarmHistory updatedAlarmHistory = alarmHistoryRepository.findOne(alarmHistory.getId());
        updatedAlarmHistory
            .date(UPDATED_DATE);
        AlarmHistoryDTO alarmHistoryDTO = alarmHistoryMapper.toDto(updatedAlarmHistory);

        restAlarmHistoryMockMvc.perform(put("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeUpdate);
        AlarmHistory testAlarmHistory = alarmHistoryList.get(alarmHistoryList.size() - 1);
        assertThat(testAlarmHistory.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the AlarmHistory in Elasticsearch
        AlarmHistory alarmHistoryEs = alarmHistorySearchRepository.findOne(testAlarmHistory.getId());
        assertThat(testAlarmHistory.getDate()).isEqualTo(testAlarmHistory.getDate());
        assertThat(alarmHistoryEs).isEqualToIgnoringGivenFields(testAlarmHistory, "date");
    }

    @Test
    public void updateNonExistingAlarmHistory() throws Exception {
        int databaseSizeBeforeUpdate = alarmHistoryRepository.findAll().size();

        // Create the AlarmHistory
        AlarmHistoryDTO alarmHistoryDTO = alarmHistoryMapper.toDto(alarmHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAlarmHistoryMockMvc.perform(put("/api/alarm-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the AlarmHistory in the database
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.save(alarmHistory);
        alarmHistorySearchRepository.save(alarmHistory);
        int databaseSizeBeforeDelete = alarmHistoryRepository.findAll().size();

        // Get the alarmHistory
        restAlarmHistoryMockMvc.perform(delete("/api/alarm-histories/{id}", alarmHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean alarmHistoryExistsInEs = alarmHistorySearchRepository.exists(alarmHistory.getId());
        assertThat(alarmHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepository.findAll();
        assertThat(alarmHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchAlarmHistory() throws Exception {
        // Initialize the database
        alarmHistoryRepository.save(alarmHistory);
        alarmHistorySearchRepository.save(alarmHistory);

        // Search the alarmHistory
        restAlarmHistoryMockMvc.perform(get("/api/_search/alarm-histories?query=id:" + alarmHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarmHistory.getId())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmHistory.class);
        AlarmHistory alarmHistory1 = new AlarmHistory();
        alarmHistory1.setId("id1");
        AlarmHistory alarmHistory2 = new AlarmHistory();
        alarmHistory2.setId(alarmHistory1.getId());
        assertThat(alarmHistory1).isEqualTo(alarmHistory2);
        alarmHistory2.setId("id2");
        assertThat(alarmHistory1).isNotEqualTo(alarmHistory2);
        alarmHistory1.setId(null);
        assertThat(alarmHistory1).isNotEqualTo(alarmHistory2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmHistoryDTO.class);
        AlarmHistoryDTO alarmHistoryDTO1 = new AlarmHistoryDTO();
        alarmHistoryDTO1.setId("id1");
        AlarmHistoryDTO alarmHistoryDTO2 = new AlarmHistoryDTO();
        assertThat(alarmHistoryDTO1).isNotEqualTo(alarmHistoryDTO2);
        alarmHistoryDTO2.setId(alarmHistoryDTO1.getId());
        assertThat(alarmHistoryDTO1).isEqualTo(alarmHistoryDTO2);
        alarmHistoryDTO2.setId("id2");
        assertThat(alarmHistoryDTO1).isNotEqualTo(alarmHistoryDTO2);
        alarmHistoryDTO1.setId(null);
        assertThat(alarmHistoryDTO1).isNotEqualTo(alarmHistoryDTO2);
    }
}
