package com.siemcore.web.rest;

import com.siemcore.SiemCoreApp;

import com.siemcore.domain.Agent;
import com.siemcore.repository.AgentRepository;
import com.siemcore.service.AgentService;
import com.siemcore.repository.search.AgentSearchRepository;
import com.siemcore.service.dto.AgentDTO;
import com.siemcore.service.mapper.AgentMapper;
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
 * Test class for the AgentResource REST controller.
 *
 * @see AgentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCoreApp.class)
public class AgentResourceIntTest {

    private static final String DEFAULT_DIRECTORY = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILTER_EXPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_EXPRESSION = "BBBBBBBBBB";

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentSearchRepository agentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAgentMockMvc;

    private Agent agent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgentResource agentResource = new AgentResource(agentService);
        this.restAgentMockMvc = MockMvcBuilders.standaloneSetup(agentResource)
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
    public static Agent createEntity() {
        Agent agent = new Agent()
            .directory(DEFAULT_DIRECTORY)
            .description(DEFAULT_DESCRIPTION)
            .filterExpression(DEFAULT_FILTER_EXPRESSION);
        return agent;
    }

    @Before
    public void initTest() {
        agentRepository.deleteAll();
        agentSearchRepository.deleteAll();
        agent = createEntity();
    }

    @Test
    public void createAgent() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().size();

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);
        restAgentMockMvc.perform(post("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isCreated());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate + 1);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getDirectory()).isEqualTo(DEFAULT_DIRECTORY);
        assertThat(testAgent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAgent.getFilterExpression()).isEqualTo(DEFAULT_FILTER_EXPRESSION);

        // Validate the Agent in Elasticsearch
        Agent agentEs = agentSearchRepository.findOne(testAgent.getId());
        assertThat(agentEs).isEqualToIgnoringGivenFields(testAgent);
    }

    @Test
    public void createAgentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().size();

        // Create the Agent with an existing ID
        agent.setId("existing_id");
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentMockMvc.perform(post("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllAgents() throws Exception {
        // Initialize the database
        agentRepository.save(agent);

        // Get all the agentList
        restAgentMockMvc.perform(get("/api/agents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId())))
            .andExpect(jsonPath("$.[*].directory").value(hasItem(DEFAULT_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].filterExpression").value(hasItem(DEFAULT_FILTER_EXPRESSION.toString())));
    }

    @Test
    public void getAgent() throws Exception {
        // Initialize the database
        agentRepository.save(agent);

        // Get the agent
        restAgentMockMvc.perform(get("/api/agents/{id}", agent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agent.getId()))
            .andExpect(jsonPath("$.directory").value(DEFAULT_DIRECTORY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.filterExpression").value(DEFAULT_FILTER_EXPRESSION.toString()));
    }

    @Test
    public void getNonExistingAgent() throws Exception {
        // Get the agent
        restAgentMockMvc.perform(get("/api/agents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAgent() throws Exception {
        // Initialize the database
        agentRepository.save(agent);
        agentSearchRepository.save(agent);
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent
        Agent updatedAgent = agentRepository.findOne(agent.getId());
        updatedAgent
            .directory(UPDATED_DIRECTORY)
            .description(UPDATED_DESCRIPTION)
            .filterExpression(UPDATED_FILTER_EXPRESSION);
        AgentDTO agentDTO = agentMapper.toDto(updatedAgent);

        restAgentMockMvc.perform(put("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getDirectory()).isEqualTo(UPDATED_DIRECTORY);
        assertThat(testAgent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAgent.getFilterExpression()).isEqualTo(UPDATED_FILTER_EXPRESSION);

        // Validate the Agent in Elasticsearch
        Agent agentEs = agentSearchRepository.findOne(testAgent.getId());
        assertThat(agentEs).isEqualToIgnoringGivenFields(testAgent);
    }

    @Test
    public void updateNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgentMockMvc.perform(put("/api/agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isCreated());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteAgent() throws Exception {
        // Initialize the database
        agentRepository.save(agent);
        agentSearchRepository.save(agent);
        int databaseSizeBeforeDelete = agentRepository.findAll().size();

        // Get the agent
        restAgentMockMvc.perform(delete("/api/agents/{id}", agent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean agentExistsInEs = agentSearchRepository.exists(agent.getId());
        assertThat(agentExistsInEs).isFalse();

        // Validate the database is empty
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchAgent() throws Exception {
        // Initialize the database
        agentRepository.save(agent);
        agentSearchRepository.save(agent);

        // Search the agent
        restAgentMockMvc.perform(get("/api/_search/agents?query=id:" + agent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId())))
            .andExpect(jsonPath("$.[*].directory").value(hasItem(DEFAULT_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].filterExpression").value(hasItem(DEFAULT_FILTER_EXPRESSION.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agent.class);
        Agent agent1 = new Agent();
        agent1.setId("id1");
        Agent agent2 = new Agent();
        agent2.setId(agent1.getId());
        assertThat(agent1).isEqualTo(agent2);
        agent2.setId("id2");
        assertThat(agent1).isNotEqualTo(agent2);
        agent1.setId(null);
        assertThat(agent1).isNotEqualTo(agent2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentDTO.class);
        AgentDTO agentDTO1 = new AgentDTO();
        agentDTO1.setId("id1");
        AgentDTO agentDTO2 = new AgentDTO();
        assertThat(agentDTO1).isNotEqualTo(agentDTO2);
        agentDTO2.setId(agentDTO1.getId());
        assertThat(agentDTO1).isEqualTo(agentDTO2);
        agentDTO2.setId("id2");
        assertThat(agentDTO1).isNotEqualTo(agentDTO2);
        agentDTO1.setId(null);
        assertThat(agentDTO1).isNotEqualTo(agentDTO2);
    }
}
