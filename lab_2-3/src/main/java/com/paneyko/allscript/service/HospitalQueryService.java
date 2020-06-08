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

import com.paneyko.allscript.domain.Hospital;
import com.paneyko.allscript.domain.*; // for static metamodels
import com.paneyko.allscript.repository.HospitalRepository;
import com.paneyko.allscript.service.dto.HospitalCriteria;

/**
 * Service for executing complex queries for {@link Hospital} entities in the database.
 * The main input is a {@link HospitalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Hospital} or a {@link Page} of {@link Hospital} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HospitalQueryService extends QueryService<Hospital> {

    private final Logger log = LoggerFactory.getLogger(HospitalQueryService.class);

    private final HospitalRepository hospitalRepository;

    public HospitalQueryService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    /**
     * Return a {@link List} of {@link Hospital} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Hospital> findByCriteria(HospitalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Hospital} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Hospital> findByCriteria(HospitalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HospitalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalRepository.count(specification);
    }

    /**
     * Function to convert {@link HospitalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Hospital> createSpecification(HospitalCriteria criteria) {
        Specification<Hospital> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Hospital_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Hospital_.name));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Hospital_.phone));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Hospital_.address));
            }
            if (criteria.getPaidFor() != null) {
                specification = specification.and(buildSpecification(criteria.getPaidFor(), Hospital_.paidFor));
            }
            if (criteria.getOncologistsId() != null) {
                specification = specification.and(buildSpecification(criteria.getOncologistsId(),
                    root -> root.join(Hospital_.oncologists, JoinType.LEFT).get(Oncologist_.id)));
            }
            if (criteria.getTherapistsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTherapistsId(),
                    root -> root.join(Hospital_.therapists, JoinType.LEFT).get(Therapist_.id)));
            }
        }
        return specification;
    }
}
