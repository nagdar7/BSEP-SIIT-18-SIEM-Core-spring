package com.siemcore.web.rest;

import com.siemcore.SiemCoreApp;

import com.siemcore.domain.Log;
import com.siemcore.repository.LogRepository;
import com.siemcore.service.AgentService;
import com.siemcore.service.LogService;
import com.siemcore.repository.search.LogSearchRepository;
import com.siemcore.service.dto.LogDTO;
import com.siemcore.service.mapper.LogMapper;
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
 * Test class for the LogResource REST controller.
 *
 * @see LogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCoreApp.class)
public class LogResourceIntTest {

    private static final Long DEFAULT_FACILITY = 1L;
    private static final Long UPDATED_FACILITY = 2L;

    private static final Long DEFAULT_SEVERNITY = 1L;
    private static final Long UPDATED_SEVERNITY = 2L;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private LogService logService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private LogSearchRepository logSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restLogMockMvc;

    private Log log;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LogResource logResource = new LogResource(logService, agentService);
        this.restLogMockMvc = MockMvcBuilders.standaloneSetup(logResource)
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
    public static Log createEntity() {
        Log log = new Log()
            .facility(DEFAULT_FACILITY)
            .severnity(DEFAULT_SEVERNITY)
            .message(DEFAULT_MESSAGE);
        return log;
    }

    @Before
    public void initTest() {
        logRepository.deleteAll();
        logSearchRepository.deleteAll();
        log = createEntity();
    }

    @Test
    public void createLog() throws Exception {
        int databaseSizeBeforeCreate = logRepository.findAll().size();

        // Create the Log
        LogDTO logDTO = logMapper.toDto(log);
        restLogMockMvc.perform(post("/api/logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(logDTO)))
            .andExpect(status().isCreated());

        // Validate the Log in the database
        List<Log> logList = logRepository.findAll();
        assertThat(logList).hasSize(databaseSizeBeforeCreate + 1);
        Log testLog = logList.get(logList.size() - 1);
        assertThat(testLog.getFacility()).isEqualTo(DEFAULT_FACILITY);
        assertThat(testLog.getSevernity()).isEqualTo(DEFAULT_SEVERNITY);
        assertThat(testLog.getMessage()).isEqualTo(DEFAULT_MESSAGE);

        // Validate the Log in Elasticsearch
        Log logEs = logSearchRepository.findOne(testLog.getId());
        assertThat(logEs).isEqualToIgnoringGivenFields(testLog);
    }

    @Test
    public void createLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = logRepository.findAll().size();

        // Create the Log with an existing ID
        log.setId("existing_id");
        LogDTO logDTO = logMapper.toDto(log);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLogMockMvc.perform(post("/api/logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(logDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Log in the database
        List<Log> logList = logRepository.findAll();
        assertThat(logList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllLogs() throws Exception {
        // Initialize the database
        logRepository.save(log);

        // Get all the logList
        restLogMockMvc.perform(get("/api/logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(log.getId())))
            .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.intValue())))
            .andExpect(jsonPath("$.[*].severnity").value(hasItem(DEFAULT_SEVERNITY.intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    public void getLog() throws Exception {
        // Initialize the database
        logRepository.save(log);

        // Get the log
        restLogMockMvc.perform(get("/api/logs/{id}", log.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(log.getId()))
            .andExpect(jsonPath("$.facility").value(DEFAULT_FACILITY.intValue()))
            .andExpect(jsonPath("$.severnity").value(DEFAULT_SEVERNITY.intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    public void getNonExistingLog() throws Exception {
        // Get the log
        restLogMockMvc.perform(get("/api/logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLog() throws Exception {
        // Initialize the database
        logRepository.save(log);
        logSearchRepository.save(log);
        int databaseSizeBeforeUpdate = logRepository.findAll().size();

        // Update the log
        Log updatedLog = logRepository.findOne(log.getId());
        updatedLog
            .facility(UPDATED_FACILITY)
            .severnity(UPDATED_SEVERNITY)
            .message(UPDATED_MESSAGE);
        LogDTO logDTO = logMapper.toDto(updatedLog);

        restLogMockMvc.perform(put("/api/logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(logDTO)))
            .andExpect(status().isOk());

        // Validate the Log in the database
        List<Log> logList = logRepository.findAll();
        assertThat(logList).hasSize(databaseSizeBeforeUpdate);
        Log testLog = logList.get(logList.size() - 1);
        assertThat(testLog.getFacility()).isEqualTo(UPDATED_FACILITY);
        assertThat(testLog.getSevernity()).isEqualTo(UPDATED_SEVERNITY);
        assertThat(testLog.getMessage()).isEqualTo(UPDATED_MESSAGE);

        // Validate the Log in Elasticsearch
        Log logEs = logSearchRepository.findOne(testLog.getId());
        assertThat(logEs).isEqualToIgnoringGivenFields(testLog);
    }

    @Test
    public void updateNonExistingLog() throws Exception {
        int databaseSizeBeforeUpdate = logRepository.findAll().size();

        // Create the Log
        LogDTO logDTO = logMapper.toDto(log);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLogMockMvc.perform(put("/api/logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(logDTO)))
            .andExpect(status().isCreated());

        // Validate the Log in the database
        List<Log> logList = logRepository.findAll();
        assertThat(logList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteLog() throws Exception {
        // Initialize the database
        logRepository.save(log);
        logSearchRepository.save(log);
        int databaseSizeBeforeDelete = logRepository.findAll().size();

        // Get the log
        restLogMockMvc.perform(delete("/api/logs/{id}", log.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean logExistsInEs = logSearchRepository.exists(log.getId());
        assertThat(logExistsInEs).isFalse();

        // Validate the database is empty
        List<Log> logList = logRepository.findAll();
        assertThat(logList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchLog() throws Exception {
        // Initialize the database
        logRepository.save(log);
        logSearchRepository.save(log);

        // Search the log
        restLogMockMvc.perform(get("/api/_search/logs?query=id:" + log.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(log.getId())))
            .andExpect(jsonPath("$.[*].facility").value(hasItem(DEFAULT_FACILITY.intValue())))
            .andExpect(jsonPath("$.[*].severnity").value(hasItem(DEFAULT_SEVERNITY.intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Log.class);
        Log log1 = new Log();
        log1.setId("id1");
        Log log2 = new Log();
        log2.setId(log1.getId());
        assertThat(log1).isEqualTo(log2);
        log2.setId("id2");
        assertThat(log1).isNotEqualTo(log2);
        log1.setId(null);
        assertThat(log1).isNotEqualTo(log2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LogDTO.class);
        LogDTO logDTO1 = new LogDTO();
        logDTO1.setId("id1");
        LogDTO logDTO2 = new LogDTO();
        assertThat(logDTO1).isNotEqualTo(logDTO2);
        logDTO2.setId(logDTO1.getId());
        assertThat(logDTO1).isEqualTo(logDTO2);
        logDTO2.setId("id2");
        assertThat(logDTO1).isNotEqualTo(logDTO2);
        logDTO1.setId(null);
        assertThat(logDTO1).isNotEqualTo(logDTO2);
    }
}
