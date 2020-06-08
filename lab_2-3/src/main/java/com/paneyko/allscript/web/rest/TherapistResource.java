package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.domain.Therapist;
import com.paneyko.allscript.service.TherapistService;
import com.paneyko.allscript.web.rest.errors.BadRequestAlertException;
import com.paneyko.allscript.service.dto.TherapistCriteria;
import com.paneyko.allscript.service.TherapistQueryService;

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
 * REST controller for managing {@link com.paneyko.allscript.domain.Therapist}.
 */
@RestController
@RequestMapping("/api")
public class TherapistResource {

    private final Logger log = LoggerFactory.getLogger(TherapistResource.class);

    private static final String ENTITY_NAME = "therapist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TherapistService therapistService;

    private final TherapistQueryService therapistQueryService;

    public TherapistResource(TherapistService therapistService, TherapistQueryService therapistQueryService) {
        this.therapistService = therapistService;
        this.therapistQueryService = therapistQueryService;
    }

    /**
     * {@code POST  /therapists} : Create a new therapist.
     *
     * @param therapist the therapist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new therapist, or with status {@code 400 (Bad Request)} if the therapist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/therapists")
    public ResponseEntity<Therapist> createTherapist(@Valid @RequestBody Therapist therapist) throws URISyntaxException {
        log.debug("REST request to save Therapist : {}", therapist);
        if (therapist.getId() != null) {
            throw new BadRequestAlertException("A new therapist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Therapist result = therapistService.save(therapist);
        return ResponseEntity.created(new URI("/api/therapists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /therapists} : Updates an existing therapist.
     *
     * @param therapist the therapist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated therapist,
     * or with status {@code 400 (Bad Request)} if the therapist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the therapist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/therapists")
    public ResponseEntity<Therapist> updateTherapist(@Valid @RequestBody Therapist therapist) throws URISyntaxException {
        log.debug("REST request to update Therapist : {}", therapist);
        if (therapist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Therapist result = therapistService.save(therapist);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, therapist.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /therapists} : get all the therapists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of therapists in body.
     */
    @GetMapping("/therapists")
    public ResponseEntity<List<Therapist>> getAllTherapists(TherapistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Therapists by criteria: {}", criteria);
        Page<Therapist> page = therapistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /therapists/count} : count all the therapists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/therapists/count")
    public ResponseEntity<Long> countTherapists(TherapistCriteria criteria) {
        log.debug("REST request to count Therapists by criteria: {}", criteria);
        return ResponseEntity.ok().body(therapistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /therapists/:id} : get the "id" therapist.
     *
     * @param id the id of the therapist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the therapist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/therapists/{id}")
    public ResponseEntity<Therapist> getTherapist(@PathVariable Long id) {
        log.debug("REST request to get Therapist : {}", id);
        Optional<Therapist> therapist = therapistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(therapist);
    }

    /**
     * {@code DELETE  /therapists/:id} : delete the "id" therapist.
     *
     * @param id the id of the therapist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/therapists/{id}")
    public ResponseEntity<Void> deleteTherapist(@PathVariable Long id) {
        log.debug("REST request to delete Therapist : {}", id);
        therapistService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
