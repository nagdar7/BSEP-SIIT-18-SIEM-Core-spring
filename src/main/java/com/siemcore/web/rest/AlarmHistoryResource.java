package com.siemcore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.siemcore.service.AlarmHistoryService;
import com.siemcore.web.rest.errors.BadRequestAlertException;
import com.siemcore.web.rest.util.HeaderUtil;
import com.siemcore.web.rest.util.PaginationUtil;
import com.siemcore.service.dto.AlarmHistoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AlarmHistory.
 */
@RestController
@RequestMapping("/api")
public class AlarmHistoryResource {

    private final Logger log = LoggerFactory.getLogger(AlarmHistoryResource.class);

    private static final String ENTITY_NAME = "alarmHistory";

    private final AlarmHistoryService alarmHistoryService;

    public AlarmHistoryResource(AlarmHistoryService alarmHistoryService) {
        this.alarmHistoryService = alarmHistoryService;
    }

    /**
     * POST  /alarm-histories : Create a new alarmHistory.
     *
     * @param alarmHistoryDTO the alarmHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alarmHistoryDTO, or with status 400 (Bad Request) if the alarmHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alarm-histories")
    @Timed
    public ResponseEntity<AlarmHistoryDTO> createAlarmHistory(@RequestBody AlarmHistoryDTO alarmHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save AlarmHistory : {}", alarmHistoryDTO);
        if (alarmHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new alarmHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlarmHistoryDTO result = alarmHistoryService.save(alarmHistoryDTO);
        return ResponseEntity.created(new URI("/api/alarm-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alarm-histories : Updates an existing alarmHistory.
     *
     * @param alarmHistoryDTO the alarmHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alarmHistoryDTO,
     * or with status 400 (Bad Request) if the alarmHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the alarmHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alarm-histories")
    @Timed
    public ResponseEntity<AlarmHistoryDTO> updateAlarmHistory(@RequestBody AlarmHistoryDTO alarmHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update AlarmHistory : {}", alarmHistoryDTO);
        if (alarmHistoryDTO.getId() == null) {
            return createAlarmHistory(alarmHistoryDTO);
        }
        AlarmHistoryDTO result = alarmHistoryService.save(alarmHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alarmHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alarm-histories : get all the alarmHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of alarmHistories in body
     */
    @GetMapping("/alarm-histories")
    @Timed
    public ResponseEntity<List<AlarmHistoryDTO>> getAllAlarmHistories(Pageable pageable) {
        log.debug("REST request to get a page of AlarmHistories");
        Page<AlarmHistoryDTO> page = alarmHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alarm-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alarm-histories/:id : get the "id" alarmHistory.
     *
     * @param id the id of the alarmHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alarmHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/alarm-histories/{id}")
    @Timed
    public ResponseEntity<AlarmHistoryDTO> getAlarmHistory(@PathVariable String id) {
        log.debug("REST request to get AlarmHistory : {}", id);
        AlarmHistoryDTO alarmHistoryDTO = alarmHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(alarmHistoryDTO));
    }

    /**
     * DELETE  /alarm-histories/:id : delete the "id" alarmHistory.
     *
     * @param id the id of the alarmHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alarm-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlarmHistory(@PathVariable String id) {
        log.debug("REST request to delete AlarmHistory : {}", id);
        alarmHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/alarm-histories?query=:query : search for the alarmHistory corresponding
     * to the query.
     *
     * @param query the query of the alarmHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/alarm-histories")
    @Timed
    public ResponseEntity<List<AlarmHistoryDTO>> searchAlarmHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AlarmHistories for query {}", query);
        Page<AlarmHistoryDTO> page = alarmHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/alarm-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
