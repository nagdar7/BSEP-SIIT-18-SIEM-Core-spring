package com.siemcore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.siemcore.service.RuleService;
import com.siemcore.web.rest.errors.BadRequestAlertException;
import com.siemcore.web.rest.util.HeaderUtil;
import com.siemcore.web.rest.util.PaginationUtil;
import com.siemcore.service.dto.RuleDTO;
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
 * REST controller for managing Rule.
 */
@RestController
@RequestMapping("/api")
public class RuleResource {

    private final Logger log = LoggerFactory.getLogger(RuleResource.class);

    private static final String ENTITY_NAME = "rule";

    private final RuleService ruleService;

    public RuleResource(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * POST  /rules : Create a new rule.
     *
     * @param ruleDTO the ruleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleDTO, or with status 400 (Bad Request) if the rule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rules")
    @Timed
    public ResponseEntity<RuleDTO> createRule(@RequestBody RuleDTO ruleDTO) throws URISyntaxException {
        log.debug("REST request to save Rule : {}", ruleDTO);
        if (ruleDTO.getId() != null) {
            throw new BadRequestAlertException("A new rule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RuleDTO result = ruleService.save(ruleDTO);
        return ResponseEntity.created(new URI("/api/rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rules : Updates an existing rule.
     *
     * @param ruleDTO the ruleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleDTO,
     * or with status 400 (Bad Request) if the ruleDTO is not valid,
     * or with status 500 (Internal Server Error) if the ruleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rules")
    @Timed
    public ResponseEntity<RuleDTO> updateRule(@RequestBody RuleDTO ruleDTO) throws URISyntaxException {
        log.debug("REST request to update Rule : {}", ruleDTO);
        if (ruleDTO.getId() == null) {
            return createRule(ruleDTO);
        }
        RuleDTO result = ruleService.save(ruleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rules : get all the rules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rules in body
     */
    @GetMapping("/rules")
    @Timed
    public ResponseEntity<List<RuleDTO>> getAllRules(Pageable pageable) {
        log.debug("REST request to get a page of Rules");
        Page<RuleDTO> page = ruleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rules/:id : get the "id" rule.
     *
     * @param id the id of the ruleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rules/{id}")
    @Timed
    public ResponseEntity<RuleDTO> getRule(@PathVariable String id) {
        log.debug("REST request to get Rule : {}", id);
        RuleDTO ruleDTO = ruleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleDTO));
    }

    /**
     * DELETE  /rules/:id : delete the "id" rule.
     *
     * @param id the id of the ruleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rules/{id}")
    @Timed
    public ResponseEntity<Void> deleteRule(@PathVariable String id) {
        log.debug("REST request to delete Rule : {}", id);
        ruleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/rules?query=:query : search for the rule corresponding
     * to the query.
     *
     * @param query the query of the rule search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rules")
    @Timed
    public ResponseEntity<List<RuleDTO>> searchRules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Rules for query {}", query);
        Page<RuleDTO> page = ruleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
