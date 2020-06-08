package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.domain.Conclusion;
import com.paneyko.allscript.service.ConclusionService;
import com.paneyko.allscript.web.rest.errors.BadRequestAlertException;
import com.paneyko.allscript.service.dto.ConclusionCriteria;
import com.paneyko.allscript.service.ConclusionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.paneyko.allscript.domain.Conclusion}.
 */
@RestController
@RequestMapping("/api")
public class ConclusionResource {

    private final Logger log = LoggerFactory.getLogger(ConclusionResource.class);

    private static final String ENTITY_NAME = "conclusion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConclusionService conclusionService;

    private final ConclusionQueryService conclusionQueryService;

    public ConclusionResource(ConclusionService conclusionService, ConclusionQueryService conclusionQueryService) {
        this.conclusionService = conclusionService;
        this.conclusionQueryService = conclusionQueryService;
    }

    /**
     * {@code POST  /conclusions} : Create a new conclusion.
     *
     * @param conclusion the conclusion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conclusion, or with status {@code 400 (Bad Request)} if the conclusion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conclusions")
    public ResponseEntity<Conclusion> createConclusion(@RequestBody Conclusion conclusion) throws URISyntaxException {
        log.debug("REST request to save Conclusion : {}", conclusion);
        if (conclusion.getId() != null) {
            throw new BadRequestAlertException("A new conclusion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conclusion result = conclusionService.save(conclusion);
        return ResponseEntity.created(new URI("/api/conclusions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conclusions} : Updates an existing conclusion.
     *
     * @param conclusion the conclusion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conclusion,
     * or with status {@code 400 (Bad Request)} if the conclusion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conclusion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conclusions")
    public ResponseEntity<Conclusion> updateConclusion(@RequestBody Conclusion conclusion) throws URISyntaxException {
        log.debug("REST request to update Conclusion : {}", conclusion);
        if (conclusion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Conclusion result = conclusionService.save(conclusion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conclusion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /conclusions} : get all the conclusions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conclusions in body.
     */
    @GetMapping("/conclusions")
    public ResponseEntity<List<Conclusion>> getAllConclusions(ConclusionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Conclusions by criteria: {}", criteria);
        Page<Conclusion> page = conclusionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conclusions/count} : count all the conclusions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/conclusions/count")
    public ResponseEntity<Long> countConclusions(ConclusionCriteria criteria) {
        log.debug("REST request to count Conclusions by criteria: {}", criteria);
        return ResponseEntity.ok().body(conclusionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conclusions/:id} : get the "id" conclusion.
     *
     * @param id the id of the conclusion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conclusion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conclusions/{id}")
    public ResponseEntity<Conclusion> getConclusion(@PathVariable Long id) {
        log.debug("REST request to get Conclusion : {}", id);
        Optional<Conclusion> conclusion = conclusionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conclusion);
    }

    /**
     * {@code DELETE  /conclusions/:id} : delete the "id" conclusion.
     *
     * @param id the id of the conclusion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conclusions/{id}")
    public ResponseEntity<Void> deleteConclusion(@PathVariable Long id) {
        log.debug("REST request to delete Conclusion : {}", id);
        conclusionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
