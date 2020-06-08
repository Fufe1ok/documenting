package com.paneyko.allscript.service;

import com.paneyko.allscript.domain.TestResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TestResult}.
 */
public interface TestResultService {

    /**
     * Save a testResult.
     *
     * @param testResult the entity to save.
     * @return the persisted entity.
     */
    TestResult save(TestResult testResult);

    /**
     * Get all the testResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestResult> findAll(Pageable pageable);

    /**
     * Get the "id" testResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestResult> findOne(Long id);

    /**
     * Delete the "id" testResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
