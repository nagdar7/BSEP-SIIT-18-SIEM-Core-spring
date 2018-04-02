package com.siemcore.service;

import com.siemcore.service.dto.AlarmRuleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing AlarmRule.
 */
public interface AlarmRuleService {

    /**
     * Save a alarmRule.
     *
     * @param alarmRuleDTO the entity to save
     * @return the persisted entity
     */
    AlarmRuleDTO save(AlarmRuleDTO alarmRuleDTO);

    /**
     * Get all the alarmRules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AlarmRuleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" alarmRule.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AlarmRuleDTO findOne(String id);

    /**
     * Delete the "id" alarmRule.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the alarmRule corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AlarmRuleDTO> search(String query, Pageable pageable);
}
