package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.domain.TestResult;
import com.paneyko.allscript.service.TestResultService;
import com.paneyko.allscript.web.rest.errors.BadRequestAlertException;
import com.paneyko.allscript.service.dto.TestResultCriteria;
import com.paneyko.allscript.service.TestResultQueryService;

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
 * REST controller for managing {@link com.paneyko.allscript.domain.TestResult}.
 */
@RestController
@RequestMapping("/api")
public class TestResultResource {

    private final Logger log = LoggerFactory.getLogger(TestResultResource.class);

    private static final String ENTITY_NAME = "testResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestResultService testResultService;

    private final TestResultQueryService testResultQueryService;

    public TestResultResource(TestResultService testResultService, TestResultQueryService testResultQueryService) {
        this.testResultService = testResultService;
        this.testResultQueryService = testResultQueryService;
    }

    /**
     * {@code POST  /test-results} : Create a new testResult.
     *
     * @param testResult the testResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testResult, or with status {@code 400 (Bad Request)} if the testResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-results")
    public ResponseEntity<TestResult> createTestResult(@RequestBody TestResult testResult) throws URISyntaxException {
        log.debug("REST request to save TestResult : {}", testResult);
        if (testResult.getId() != null) {
            throw new BadRequestAlertException("A new testResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestResult result = testResultService.save(testResult);
        return ResponseEntity.created(new URI("/api/test-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-results} : Updates an existing testResult.
     *
     * @param testResult the testResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testResult,
     * or with status {@code 400 (Bad Request)} if the testResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-results")
    public ResponseEntity<TestResult> updateTestResult(@RequestBody TestResult testResult) throws URISyntaxException {
        log.debug("REST request to update TestResult : {}", testResult);
        if (testResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TestResult result = testResultService.save(testResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testResult.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /test-results} : get all the testResults.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testResults in body.
     */
    @GetMapping("/test-results")
    public ResponseEntity<List<TestResult>> getAllTestResults(TestResultCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TestResults by criteria: {}", criteria);
        Page<TestResult> page = testResultQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-results/count} : count all the testResults.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-results/count")
    public ResponseEntity<Long> countTestResults(TestResultCriteria criteria) {
        log.debug("REST request to count TestResults by criteria: {}", criteria);
        return ResponseEntity.ok().body(testResultQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-results/:id} : get the "id" testResult.
     *
     * @param id the id of the testResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-results/{id}")
    public ResponseEntity<TestResult> getTestResult(@PathVariable Long id) {
        log.debug("REST request to get TestResult : {}", id);
        Optional<TestResult> testResult = testResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testResult);
    }

    /**
     * {@code DELETE  /test-results/:id} : delete the "id" testResult.
     *
     * @param id the id of the testResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-results/{id}")
    public ResponseEntity<Void> deleteTestResult(@PathVariable Long id) {
        log.debug("REST request to delete TestResult : {}", id);
        testResultService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
