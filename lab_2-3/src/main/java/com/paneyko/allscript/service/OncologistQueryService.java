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

import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.domain.*; // for static metamodels
import com.paneyko.allscript.repository.OncologistRepository;
import com.paneyko.allscript.service.dto.OncologistCriteria;

/**
 * Service for executing complex queries for {@link Oncologist} entities in the database.
 * The main input is a {@link OncologistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Oncologist} or a {@link Page} of {@link Oncologist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OncologistQueryService extends QueryService<Oncologist> {

    private final Logger log = LoggerFactory.getLogger(OncologistQueryService.class);

    private final OncologistRepository oncologistRepository;

    public OncologistQueryService(OncologistRepository oncologistRepository) {
        this.oncologistRepository = oncologistRepository;
    }

    /**
     * Return a {@link List} of {@link Oncologist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Oncologist> findByCriteria(OncologistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Oncologist> specification = createSpecification(criteria);
        return oncologistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Oncologist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Oncologist> findByCriteria(OncologistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Oncologist> specification = createSpecification(criteria);
        return oncologistRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OncologistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Oncologist> specification = createSpecification(criteria);
        return oncologistRepository.count(specification);
    }

    /**
     * Function to convert {@link OncologistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Oncologist> createSpecification(OncologistCriteria criteria) {
        Specification<Oncologist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Oncologist_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Oncologist_.name));
            }
            if (criteria.getSurname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSurname(), Oncologist_.surname));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Oncologist_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Oncologist_.phone));
            }
            if (criteria.getRoomIn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoomIn(), Oncologist_.roomIn));
            }
            if (criteria.getConclusionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getConclusionsId(),
                    root -> root.join(Oncologist_.conclusions, JoinType.LEFT).get(Conclusion_.id)));
            }
            if (criteria.getTestsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTestsId(),
                    root -> root.join(Oncologist_.tests, JoinType.LEFT).get(TestResult_.id)));
            }
            if (criteria.getHospitalId() != null) {
                specification = specification.and(buildSpecification(criteria.getHospitalId(),
                    root -> root.join(Oncologist_.hospital, JoinType.LEFT).get(Hospital_.id)));
            }
            if (criteria.getPatientsId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientsId(),
                    root -> root.join(Oncologist_.patients, JoinType.LEFT).get(Patient_.id)));
            }
        }
        return specification;
    }
}
