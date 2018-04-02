package com.siemcore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.siemcore.service.AlarmService;
import com.siemcore.web.rest.errors.BadRequestAlertException;
import com.siemcore.web.rest.util.HeaderUtil;
import com.siemcore.web.rest.util.PaginationUtil;
import com.siemcore.service.dto.AlarmDTO;
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
 * REST controller for managing Alarm.
 */
@RestController
@RequestMapping("/api")
public class AlarmResource {

    private final Logger log = LoggerFactory.getLogger(AlarmResource.class);

    private static final String ENTITY_NAME = "alarm";

    private final AlarmService alarmService;

    public AlarmResource(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    /**
     * POST  /alarms : Create a new alarm.
     *
     * @param alarmDTO the alarmDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alarmDTO, or with status 400 (Bad Request) if the alarm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alarms")
    @Timed
    public ResponseEntity<AlarmDTO> createAlarm(@RequestBody AlarmDTO alarmDTO) throws URISyntaxException {
        log.debug("REST request to save Alarm : {}", alarmDTO);
        if (alarmDTO.getId() != null) {
            throw new BadRequestAlertException("A new alarm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlarmDTO result = alarmService.save(alarmDTO);
        return ResponseEntity.created(new URI("/api/alarms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alarms : Updates an existing alarm.
     *
     * @param alarmDTO the alarmDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alarmDTO,
     * or with status 400 (Bad Request) if the alarmDTO is not valid,
     * or with status 500 (Internal Server Error) if the alarmDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alarms")
    @Timed
    public ResponseEntity<AlarmDTO> updateAlarm(@RequestBody AlarmDTO alarmDTO) throws URISyntaxException {
        log.debug("REST request to update Alarm : {}", alarmDTO);
        if (alarmDTO.getId() == null) {
            return createAlarm(alarmDTO);
        }
        AlarmDTO result = alarmService.save(alarmDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alarmDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alarms : get all the alarms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of alarms in body
     */
    @GetMapping("/alarms")
    @Timed
    public ResponseEntity<List<AlarmDTO>> getAllAlarms(Pageable pageable) {
        log.debug("REST request to get a page of Alarms");
        Page<AlarmDTO> page = alarmService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alarms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alarms/:id : get the "id" alarm.
     *
     * @param id the id of the alarmDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alarmDTO, or with status 404 (Not Found)
     */
    @GetMapping("/alarms/{id}")
    @Timed
    public ResponseEntity<AlarmDTO> getAlarm(@PathVariable String id) {
        log.debug("REST request to get Alarm : {}", id);
        AlarmDTO alarmDTO = alarmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(alarmDTO));
    }

    /**
     * DELETE  /alarms/:id : delete the "id" alarm.
     *
     * @param id the id of the alarmDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alarms/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlarm(@PathVariable String id) {
        log.debug("REST request to delete Alarm : {}", id);
        alarmService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/alarms?query=:query : search for the alarm corresponding
     * to the query.
     *
     * @param query the query of the alarm search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/alarms")
    @Timed
    public ResponseEntity<List<AlarmDTO>> searchAlarms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Alarms for query {}", query);
        Page<AlarmDTO> page = alarmService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/alarms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
