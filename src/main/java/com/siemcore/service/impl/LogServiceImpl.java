package com.siemcore.service.impl;

import com.siemcore.service.LogService;
import com.siemcore.domain.Log;
import com.siemcore.repository.LogRepository;
import com.siemcore.repository.search.LogSearchRepository;
import com.siemcore.service.dto.LogDTO;
import com.siemcore.service.mapper.LogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Log.
 */
@Service
public class LogServiceImpl implements LogService{

    private final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    private final LogRepository logRepository;

    private final LogMapper logMapper;

    private final LogSearchRepository logSearchRepository;

    public LogServiceImpl(LogRepository logRepository, LogMapper logMapper, LogSearchRepository logSearchRepository) {
        this.logRepository = logRepository;
        this.logMapper = logMapper;
        this.logSearchRepository = logSearchRepository;
    }

    /**
     * Save a log.
     *
     * @param logDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LogDTO save(LogDTO logDTO) {
        log.debug("Request to save Log : {}", logDTO);
        Log log = logMapper.toEntity(logDTO);
        log = logRepository.save(log);
        LogDTO result = logMapper.toDto(log);
        logSearchRepository.save(log);
        return result;
    }

    /**
     * Get all the logs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<LogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Logs");
        return logRepository.findAll(pageable)
            .map(logMapper::toDto);
    }

    /**
     * Get one log by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public LogDTO findOne(String id) {
        log.debug("Request to get Log : {}", id);
        Log log = logRepository.findOne(id);
        return logMapper.toDto(log);
    }

    /**
     * Delete the log by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Log : {}", id);
        logRepository.delete(id);
        logSearchRepository.delete(id);
    }

    /**
     * Search for the log corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<LogDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Logs for query {}", query);
        Page<Log> result = logSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(logMapper::toDto);
    }
}
