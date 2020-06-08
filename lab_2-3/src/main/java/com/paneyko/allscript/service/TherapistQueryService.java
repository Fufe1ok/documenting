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

import com.paneyko.allscript.domain.Therapist;
import com.paneyko.allscript.domain.*; // for static metamodels
import com.paneyko.allscript.repository.TherapistRepository;
import com.paneyko.allscript.service.dto.TherapistCriteria;

/**
 * Service for executing complex queries for {@link Therapist} entities in the database.
 * The main input is a {@link TherapistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Therapist} or a {@link Page} of {@link Therapist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TherapistQueryService extends QueryService<Therapist> {

    private final Logger log = LoggerFactory.getLogger(TherapistQueryService.class);

    private final TherapistRepository therapistRepository;

    public TherapistQueryService(TherapistRepository therapistRepository) {
        this.therapistRepository = therapistRepository;
    }

    /**
     * Return a {@link List} of {@link Therapist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Therapist> findByCriteria(TherapistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Therapist> specification = createSpecification(criteria);
        return therapistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Therapist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Therapist> findByCriteria(TherapistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Therapist> specification = createSpecification(criteria);
        return therapistRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TherapistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Therapist> specification = createSpecification(criteria);
        return therapistRepository.count(specification);
    }

    /**
     * Function to convert {@link TherapistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Therapist> createSpecification(TherapistCriteria criteria) {
        Specification<Therapist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Therapist_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Therapist_.name));
            }
            if (criteria.getSurname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSurname(), Therapist_.surname));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Therapist_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Therapist_.phone));
            }
            if (criteria.getRoomIn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoomIn(), Therapist_.roomIn));
            }
            if (criteria.getHospitalId() != null) {
                specification = specification.and(buildSpecification(criteria.getHospitalId(),
                    root -> root.join(Therapist_.hospital, JoinType.LEFT).get(Hospital_.id)));
            }
            if (criteria.getPatientsId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientsId(),
                    root -> root.join(Therapist_.patients, JoinType.LEFT).get(Patient_.id)));
            }
        }
        return specification;
    }
}
