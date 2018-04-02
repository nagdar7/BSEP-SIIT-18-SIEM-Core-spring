package com.siemcore.web.rest;

import com.siemcore.SiemCoreApp;

import com.siemcore.domain.Alarm;
import com.siemcore.repository.AlarmRepository;
import com.siemcore.service.AlarmService;
import com.siemcore.repository.search.AlarmSearchRepository;
import com.siemcore.service.dto.AlarmDTO;
import com.siemcore.service.mapper.AlarmMapper;
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

import com.siemcore.domain.enumeration.ALARM_STATUS;
/**
 * Test class for the AlarmResource REST controller.
 *
 * @see AlarmResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCoreApp.class)
public class AlarmResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ALARM_STATUS DEFAULT_STATUS = ALARM_STATUS.ACTIVE;
    private static final ALARM_STATUS UPDATED_STATUS = ALARM_STATUS.NOT_ACTIVE;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AlarmSearchRepository alarmSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAlarmMockMvc;

    private Alarm alarm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlarmResource alarmResource = new AlarmResource(alarmService);
        this.restAlarmMockMvc = MockMvcBuilders.standaloneSetup(alarmResource)
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
    public static Alarm createEntity() {
        Alarm alarm = new Alarm()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return alarm;
    }

    @Before
    public void initTest() {
        alarmRepository.deleteAll();
        alarmSearchRepository.deleteAll();
        alarm = createEntity();
    }

    @Test
    public void createAlarm() throws Exception {
        int databaseSizeBeforeCreate = alarmRepository.findAll().size();

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);
        restAlarmMockMvc.perform(post("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmDTO)))
            .andExpect(status().isCreated());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeCreate + 1);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlarm.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Alarm in Elasticsearch
        Alarm alarmEs = alarmSearchRepository.findOne(testAlarm.getId());
        assertThat(alarmEs).isEqualToIgnoringGivenFields(testAlarm);
    }

    @Test
    public void createAlarmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alarmRepository.findAll().size();

        // Create the Alarm with an existing ID
        alarm.setId("existing_id");
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmMockMvc.perform(post("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllAlarms() throws Exception {
        // Initialize the database
        alarmRepository.save(alarm);

        // Get all the alarmList
        restAlarmMockMvc.perform(get("/api/alarms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarm.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    public void getAlarm() throws Exception {
        // Initialize the database
        alarmRepository.save(alarm);

        // Get the alarm
        restAlarmMockMvc.perform(get("/api/alarms/{id}", alarm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alarm.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    public void getNonExistingAlarm() throws Exception {
        // Get the alarm
        restAlarmMockMvc.perform(get("/api/alarms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAlarm() throws Exception {
        // Initialize the database
        alarmRepository.save(alarm);
        alarmSearchRepository.save(alarm);
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();

        // Update the alarm
        Alarm updatedAlarm = alarmRepository.findOne(alarm.getId());
        updatedAlarm
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        AlarmDTO alarmDTO = alarmMapper.toDto(updatedAlarm);

        restAlarmMockMvc.perform(put("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmDTO)))
            .andExpect(status().isOk());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlarm.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Alarm in Elasticsearch
        Alarm alarmEs = alarmSearchRepository.findOne(testAlarm.getId());
        assertThat(alarmEs).isEqualToIgnoringGivenFields(testAlarm);
    }

    @Test
    public void updateNonExistingAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAlarmMockMvc.perform(put("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarmDTO)))
            .andExpect(status().isCreated());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteAlarm() throws Exception {
        // Initialize the database
        alarmRepository.save(alarm);
        alarmSearchRepository.save(alarm);
        int databaseSizeBeforeDelete = alarmRepository.findAll().size();

        // Get the alarm
        restAlarmMockMvc.perform(delete("/api/alarms/{id}", alarm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean alarmExistsInEs = alarmSearchRepository.exists(alarm.getId());
        assertThat(alarmExistsInEs).isFalse();

        // Validate the database is empty
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchAlarm() throws Exception {
        // Initialize the database
        alarmRepository.save(alarm);
        alarmSearchRepository.save(alarm);

        // Search the alarm
        restAlarmMockMvc.perform(get("/api/_search/alarms?query=id:" + alarm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarm.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alarm.class);
        Alarm alarm1 = new Alarm();
        alarm1.setId("id1");
        Alarm alarm2 = new Alarm();
        alarm2.setId(alarm1.getId());
        assertThat(alarm1).isEqualTo(alarm2);
        alarm2.setId("id2");
        assertThat(alarm1).isNotEqualTo(alarm2);
        alarm1.setId(null);
        assertThat(alarm1).isNotEqualTo(alarm2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmDTO.class);
        AlarmDTO alarmDTO1 = new AlarmDTO();
        alarmDTO1.setId("id1");
        AlarmDTO alarmDTO2 = new AlarmDTO();
        assertThat(alarmDTO1).isNotEqualTo(alarmDTO2);
        alarmDTO2.setId(alarmDTO1.getId());
        assertThat(alarmDTO1).isEqualTo(alarmDTO2);
        alarmDTO2.setId("id2");
        assertThat(alarmDTO1).isNotEqualTo(alarmDTO2);
        alarmDTO1.setId(null);
        assertThat(alarmDTO1).isNotEqualTo(alarmDTO2);
    }
}
