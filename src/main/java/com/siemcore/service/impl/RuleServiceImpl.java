package com.siemcore.service.impl;

import com.siemcore.service.RuleService;
import com.siemcore.domain.Rule;
import com.siemcore.repository.RuleRepository;
import com.siemcore.repository.search.RuleSearchRepository;
import com.siemcore.service.dto.RuleDTO;
import com.siemcore.service.mapper.RuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Rule.
 */
@Service
public class RuleServiceImpl implements RuleService{

    private final Logger log = LoggerFactory.getLogger(RuleServiceImpl.class);

    private final RuleRepository ruleRepository;

    private final RuleMapper ruleMapper;

    private final RuleSearchRepository ruleSearchRepository;

    public RuleServiceImpl(RuleRepository ruleRepository, RuleMapper ruleMapper, RuleSearchRepository ruleSearchRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleMapper = ruleMapper;
        this.ruleSearchRepository = ruleSearchRepository;
    }

    /**
     * Save a rule.
     *
     * @param ruleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RuleDTO save(RuleDTO ruleDTO) {
        log.debug("Request to save Rule : {}", ruleDTO);
        Rule rule = ruleMapper.toEntity(ruleDTO);
        rule = ruleRepository.save(rule);
        RuleDTO result = ruleMapper.toDto(rule);
        ruleSearchRepository.save(rule);
        return result;
    }

    /**
     * Get all the rules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<RuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rules");
        return ruleRepository.findAll(pageable)
            .map(ruleMapper::toDto);
    }

    /**
     * Get one rule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public RuleDTO findOne(String id) {
        log.debug("Request to get Rule : {}", id);
        Rule rule = ruleRepository.findOne(id);
        return ruleMapper.toDto(rule);
    }

    /**
     * Delete the rule by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Rule : {}", id);
        ruleRepository.delete(id);
        ruleSearchRepository.delete(id);
    }

    /**
     * Search for the rule corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<RuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Rules for query {}", query);
        Page<Rule> result = ruleSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(ruleMapper::toDto);
    }
}
