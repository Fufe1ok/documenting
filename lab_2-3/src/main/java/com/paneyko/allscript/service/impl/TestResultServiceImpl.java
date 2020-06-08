package com.paneyko.allscript.service.impl;

import com.paneyko.allscript.service.TestResultService;
import com.paneyko.allscript.domain.TestResult;
import com.paneyko.allscript.repository.TestResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TestResult}.
 */
@Service
@Transactional
public class TestResultServiceImpl implements TestResultService {

    private final Logger log = LoggerFactory.getLogger(TestResultServiceImpl.class);

    private final TestResultRepository testResultRepository;

    public TestResultServiceImpl(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    /**
     * Save a testResult.
     *
     * @param testResult the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TestResult save(TestResult testResult) {
        log.debug("Request to save TestResult : {}", testResult);
        return testResultRepository.save(testResult);
    }

    /**
     * Get all the testResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestResult> findAll(Pageable pageable) {
        log.debug("Request to get all TestResults");
        return testResultRepository.findAll(pageable);
    }

    /**
     * Get one testResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TestResult> findOne(Long id) {
        log.debug("Request to get TestResult : {}", id);
        return testResultRepository.findById(id);
    }

    /**
     * Delete the testResult by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestResult : {}", id);
        testResultRepository.deleteById(id);
    }
}
