package com.siemcore.service;

import com.siemcore.service.dto.AgentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Agent.
 */
public interface AgentService {

    /**
     * Save a agent.
     *
     * @param agentDTO the entity to save
     * @return the persisted entity
     */
    AgentDTO save(AgentDTO agentDTO);

    /**
     * Get all the agents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AgentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" agent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AgentDTO findOne(String id);

    /**
     * Delete the "id" agent.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the agent corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AgentDTO> search(String query, Pageable pageable);
}
