package com.siemcore.service.impl;

import com.siemcore.service.AlarmHistoryService;
import com.siemcore.domain.AlarmHistory;
import com.siemcore.repository.AlarmHistoryRepository;
import com.siemcore.repository.search.AlarmHistorySearchRepository;
import com.siemcore.service.dto.AlarmHistoryDTO;
import com.siemcore.service.mapper.AlarmHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AlarmHistory.
 */
@Service
public class AlarmHistoryServiceImpl implements AlarmHistoryService{

    private final Logger log = LoggerFactory.getLogger(AlarmHistoryServiceImpl.class);

    private final AlarmHistoryRepository alarmHistoryRepository;

    private final AlarmHistoryMapper alarmHistoryMapper;

    private final AlarmHistorySearchRepository alarmHistorySearchRepository;

    public AlarmHistoryServiceImpl(AlarmHistoryRepository alarmHistoryRepository, AlarmHistoryMapper alarmHistoryMapper, AlarmHistorySearchRepository alarmHistorySearchRepository) {
        this.alarmHistoryRepository = alarmHistoryRepository;
        this.alarmHistoryMapper = alarmHistoryMapper;
        this.alarmHistorySearchRepository = alarmHistorySearchRepository;
    }

    /**
     * Save a alarmHistory.
     *
     * @param alarmHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AlarmHistoryDTO save(AlarmHistoryDTO alarmHistoryDTO) {
        log.debug("Request to save AlarmHistory : {}", alarmHistoryDTO);
        AlarmHistory alarmHistory = alarmHistoryMapper.toEntity(alarmHistoryDTO);
        alarmHistory = alarmHistoryRepository.save(alarmHistory);
        AlarmHistoryDTO result = alarmHistoryMapper.toDto(alarmHistory);
        alarmHistorySearchRepository.save(alarmHistory);
        return result;
    }

    /**
     * Get all the alarmHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<AlarmHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AlarmHistories");
        return alarmHistoryRepository.findAll(pageable)
            .map(alarmHistoryMapper::toDto);
    }

    /**
     * Get one alarmHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public AlarmHistoryDTO findOne(String id) {
        log.debug("Request to get AlarmHistory : {}", id);
        AlarmHistory alarmHistory = alarmHistoryRepository.findOne(id);
        return alarmHistoryMapper.toDto(alarmHistory);
    }

    /**
     * Delete the alarmHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete AlarmHistory : {}", id);
        alarmHistoryRepository.delete(id);
        alarmHistorySearchRepository.delete(id);
    }

    /**
     * Search for the alarmHistory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<AlarmHistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AlarmHistories for query {}", query);
        Page<AlarmHistory> result = alarmHistorySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(alarmHistoryMapper::toDto);
    }
}
