package com.siemcore.service.impl;

import com.siemcore.service.AlarmRuleService;
import com.siemcore.domain.AlarmRule;
import com.siemcore.repository.AlarmRuleRepository;
import com.siemcore.repository.search.AlarmRuleSearchRepository;
import com.siemcore.service.dto.AlarmRuleDTO;
import com.siemcore.service.mapper.AlarmRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AlarmRule.
 */
@Service
public class AlarmRuleServiceImpl implements AlarmRuleService{

    private final Logger log = LoggerFactory.getLogger(AlarmRuleServiceImpl.class);

    private final AlarmRuleRepository alarmRuleRepository;

    private final AlarmRuleMapper alarmRuleMapper;

    private final AlarmRuleSearchRepository alarmRuleSearchRepository;

    public AlarmRuleServiceImpl(AlarmRuleRepository alarmRuleRepository, AlarmRuleMapper alarmRuleMapper, AlarmRuleSearchRepository alarmRuleSearchRepository) {
        this.alarmRuleRepository = alarmRuleRepository;
        this.alarmRuleMapper = alarmRuleMapper;
        this.alarmRuleSearchRepository = alarmRuleSearchRepository;
    }

    /**
     * Save a alarmRule.
     *
     * @param alarmRuleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AlarmRuleDTO save(AlarmRuleDTO alarmRuleDTO) {
        log.debug("Request to save AlarmRule : {}", alarmRuleDTO);
        AlarmRule alarmRule = alarmRuleMapper.toEntity(alarmRuleDTO);
        alarmRule = alarmRuleRepository.save(alarmRule);
        AlarmRuleDTO result = alarmRuleMapper.toDto(alarmRule);
        alarmRuleSearchRepository.save(alarmRule);
        return result;
    }

    /**
     * Get all the alarmRules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<AlarmRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AlarmRules");
        return alarmRuleRepository.findAll(pageable)
            .map(alarmRuleMapper::toDto);
    }

    /**
     * Get one alarmRule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public AlarmRuleDTO findOne(String id) {
        log.debug("Request to get AlarmRule : {}", id);
        AlarmRule alarmRule = alarmRuleRepository.findOne(id);
        return alarmRuleMapper.toDto(alarmRule);
    }

    /**
     * Delete the alarmRule by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete AlarmRule : {}", id);
        alarmRuleRepository.delete(id);
        alarmRuleSearchRepository.delete(id);
    }

    /**
     * Search for the alarmRule corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<AlarmRuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AlarmRules for query {}", query);
        Page<AlarmRule> result = alarmRuleSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(alarmRuleMapper::toDto);
    }
}
