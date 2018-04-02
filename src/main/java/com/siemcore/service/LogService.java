package com.siemcore.service;

import com.siemcore.service.dto.LogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Log.
 */
public interface LogService {

    /**
     * Save a log.
     *
     * @param logDTO the entity to save
     * @return the persisted entity
     */
    LogDTO save(LogDTO logDTO);

    /**
     * Get all the logs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" log.
     *
     * @param id the id of the entity
     * @return the entity
     */
    LogDTO findOne(String id);

    /**
     * Delete the "id" log.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the log corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LogDTO> search(String query, Pageable pageable);
}
