package com.siemcore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.siemcore.service.AlarmRuleService;
import com.siemcore.web.rest.errors.BadRequestAlertException;
import com.siemcore.web.rest.util.HeaderUtil;
import com.siemcore.web.rest.util.PaginationUtil;
import com.siemcore.service.dto.AlarmRuleDTO;
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
 * REST controller for managing AlarmRule.
 */
@RestController
@RequestMapping("/api")
public class AlarmRuleResource {

    private final Logger log = LoggerFactory.getLogger(AlarmRuleResource.class);

    private static final String ENTITY_NAME = "alarmRule";

    private final AlarmRuleService alarmRuleService;

    public AlarmRuleResource(AlarmRuleService alarmRuleService) {
        this.alarmRuleService = alarmRuleService;
    }

    /**
     * POST  /alarm-rules : Create a new alarmRule.
     *
     * @param alarmRuleDTO the alarmRuleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alarmRuleDTO, or with status 400 (Bad Request) if the alarmRule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alarm-rules")
    @Timed
    public ResponseEntity<AlarmRuleDTO> createAlarmRule(@RequestBody AlarmRuleDTO alarmRuleDTO) throws URISyntaxException {
        log.debug("REST request to save AlarmRule : {}", alarmRuleDTO);
        if (alarmRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new alarmRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlarmRuleDTO result = alarmRuleService.save(alarmRuleDTO);
        return ResponseEntity.created(new URI("/api/alarm-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alarm-rules : Updates an existing alarmRule.
     *
     * @param alarmRuleDTO the alarmRuleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alarmRuleDTO,
     * or with status 400 (Bad Request) if the alarmRuleDTO is not valid,
     * or with status 500 (Internal Server Error) if the alarmRuleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alarm-rules")
    @Timed
    public ResponseEntity<AlarmRuleDTO> updateAlarmRule(@RequestBody AlarmRuleDTO alarmRuleDTO) throws URISyntaxException {
        log.debug("REST request to update AlarmRule : {}", alarmRuleDTO);
        if (alarmRuleDTO.getId() == null) {
            return createAlarmRule(alarmRuleDTO);
        }
        AlarmRuleDTO result = alarmRuleService.save(alarmRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alarmRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alarm-rules : get all the alarmRules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of alarmRules in body
     */
    @GetMapping("/alarm-rules")
    @Timed
    public ResponseEntity<List<AlarmRuleDTO>> getAllAlarmRules(Pageable pageable) {
        log.debug("REST request to get a page of AlarmRules");
        Page<AlarmRuleDTO> page = alarmRuleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alarm-rules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alarm-rules/:id : get the "id" alarmRule.
     *
     * @param id the id of the alarmRuleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alarmRuleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/alarm-rules/{id}")
    @Timed
    public ResponseEntity<AlarmRuleDTO> getAlarmRule(@PathVariable String id) {
        log.debug("REST request to get AlarmRule : {}", id);
        AlarmRuleDTO alarmRuleDTO = alarmRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(alarmRuleDTO));
    }

    /**
     * DELETE  /alarm-rules/:id : delete the "id" alarmRule.
     *
     * @param id the id of the alarmRuleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alarm-rules/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlarmRule(@PathVariable String id) {
        log.debug("REST request to delete AlarmRule : {}", id);
        alarmRuleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/alarm-rules?query=:query : search for the alarmRule corresponding
     * to the query.
     *
     * @param query the query of the alarmRule search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/alarm-rules")
    @Timed
    public ResponseEntity<List<AlarmRuleDTO>> searchAlarmRules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AlarmRules for query {}", query);
        Page<AlarmRuleDTO> page = alarmRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/alarm-rules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
