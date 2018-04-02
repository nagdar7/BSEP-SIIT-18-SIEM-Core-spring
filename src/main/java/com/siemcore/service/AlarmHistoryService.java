package com.siemcore.service;

import com.siemcore.service.dto.AlarmHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing AlarmHistory.
 */
public interface AlarmHistoryService {

    /**
     * Save a alarmHistory.
     *
     * @param alarmHistoryDTO the entity to save
     * @return the persisted entity
     */
    AlarmHistoryDTO save(AlarmHistoryDTO alarmHistoryDTO);

    /**
     * Get all the alarmHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AlarmHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" alarmHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AlarmHistoryDTO findOne(String id);

    /**
     * Delete the "id" alarmHistory.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the alarmHistory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AlarmHistoryDTO> search(String query, Pageable pageable);
}
