package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.service.OncologistService;
import com.paneyko.allscript.web.rest.errors.BadRequestAlertException;
import com.paneyko.allscript.service.dto.OncologistCriteria;
import com.paneyko.allscript.service.OncologistQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.paneyko.allscript.domain.Oncologist}.
 */
@RestController
@RequestMapping("/api")
public class OncologistResource {

    private final Logger log = LoggerFactory.getLogger(OncologistResource.class);

    private static final String ENTITY_NAME = "oncologist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OncologistService oncologistService;

    private final OncologistQueryService oncologistQueryService;

    public OncologistResource(OncologistService oncologistService, OncologistQueryService oncologistQueryService) {
        this.oncologistService = oncologistService;
        this.oncologistQueryService = oncologistQueryService;
    }

    /**
     * {@code POST  /oncologists} : Create a new oncologist.
     *
     * @param oncologist the oncologist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oncologist, or with status {@code 400 (Bad Request)} if the oncologist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/oncologists")
    public ResponseEntity<Oncologist> createOncologist(@Valid @RequestBody Oncologist oncologist) throws URISyntaxException {
        log.debug("REST request to save Oncologist : {}", oncologist);
        if (oncologist.getId() != null) {
            throw new BadRequestAlertException("A new oncologist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Oncologist result = oncologistService.save(oncologist);
        return ResponseEntity.created(new URI("/api/oncologists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oncologists} : Updates an existing oncologist.
     *
     * @param oncologist the oncologist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oncologist,
     * or with status {@code 400 (Bad Request)} if the oncologist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oncologist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oncologists")
    public ResponseEntity<Oncologist> updateOncologist(@Valid @RequestBody Oncologist oncologist) throws URISyntaxException {
        log.debug("REST request to update Oncologist : {}", oncologist);
        if (oncologist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Oncologist result = oncologistService.save(oncologist);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oncologist.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /oncologists} : get all the oncologists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oncologists in body.
     */
    @GetMapping("/oncologists")
    public ResponseEntity<List<Oncologist>> getAllOncologists(OncologistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Oncologists by criteria: {}", criteria);
        Page<Oncologist> page = oncologistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /oncologists/count} : count all the oncologists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/oncologists/count")
    public ResponseEntity<Long> countOncologists(OncologistCriteria criteria) {
        log.debug("REST request to count Oncologists by criteria: {}", criteria);
        return ResponseEntity.ok().body(oncologistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /oncologists/:id} : get the "id" oncologist.
     *
     * @param id the id of the oncologist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oncologist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/oncologists/{id}")
    public ResponseEntity<Oncologist> getOncologist(@PathVariable Long id) {
        log.debug("REST request to get Oncologist : {}", id);
        Optional<Oncologist> oncologist = oncologistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oncologist);
    }

    /**
     * {@code DELETE  /oncologists/:id} : delete the "id" oncologist.
     *
     * @param id the id of the oncologist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oncologists/{id}")
    public ResponseEntity<Void> deleteOncologist(@PathVariable Long id) {
        log.debug("REST request to delete Oncologist : {}", id);
        oncologistService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
