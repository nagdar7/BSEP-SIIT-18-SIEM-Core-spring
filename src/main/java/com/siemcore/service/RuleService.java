package com.siemcore.service;

import com.siemcore.service.dto.RuleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Rule.
 */
public interface RuleService {

    /**
     * Save a rule.
     *
     * @param ruleDTO the entity to save
     * @return the persisted entity
     */
    RuleDTO save(RuleDTO ruleDTO);

    /**
     * Get all the rules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RuleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rule.
     *
     * @param id the id of the entity
     * @return the entity
     */
    RuleDTO findOne(String id);

    /**
     * Delete the "id" rule.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the rule corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RuleDTO> search(String query, Pageable pageable);
}
