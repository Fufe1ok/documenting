package com.paneyko.allscript.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.paneyko.allscript.domain.TestResult;
import com.paneyko.allscript.domain.*; // for static metamodels
import com.paneyko.allscript.repository.TestResultRepository;
import com.paneyko.allscript.service.dto.TestResultCriteria;

/**
 * Service for executing complex queries for {@link TestResult} entities in the database.
 * The main input is a {@link TestResultCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestResult} or a {@link Page} of {@link TestResult} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestResultQueryService extends QueryService<TestResult> {

    private final Logger log = LoggerFactory.getLogger(TestResultQueryService.class);

    private final TestResultRepository testResultRepository;

    public TestResultQueryService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    /**
     * Return a {@link List} of {@link TestResult} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByCriteria(TestResultCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestResult> specification = createSpecification(criteria);
        return testResultRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestResult} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestResult> findByCriteria(TestResultCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestResult> specification = createSpecification(criteria);
        return testResultRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestResultCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestResult> specification = createSpecification(criteria);
        return testResultRepository.count(specification);
    }

    /**
     * Function to convert {@link TestResultCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestResult> createSpecification(TestResultCriteria criteria) {
        Specification<TestResult> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestResult_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestResult_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), TestResult_.type));
            }
            if (criteria.getDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTime(), TestResult_.dateTime));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TestResult_.description));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), TestResult_.url));
            }
            if (criteria.getOncologistId() != null) {
                specification = specification.and(buildSpecification(criteria.getOncologistId(),
                    root -> root.join(TestResult_.oncologist, JoinType.LEFT).get(Oncologist_.id)));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientId(),
                    root -> root.join(TestResult_.patient, JoinType.LEFT).get(Patient_.id)));
            }
        }
        return specification;
    }
}
