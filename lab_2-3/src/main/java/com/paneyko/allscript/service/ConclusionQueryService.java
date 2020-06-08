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

import com.paneyko.allscript.domain.Conclusion;
import com.paneyko.allscript.domain.*; // for static metamodels
import com.paneyko.allscript.repository.ConclusionRepository;
import com.paneyko.allscript.service.dto.ConclusionCriteria;

/**
 * Service for executing complex queries for {@link Conclusion} entities in the database.
 * The main input is a {@link ConclusionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Conclusion} or a {@link Page} of {@link Conclusion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConclusionQueryService extends QueryService<Conclusion> {

    private final Logger log = LoggerFactory.getLogger(ConclusionQueryService.class);

    private final ConclusionRepository conclusionRepository;

    public ConclusionQueryService(ConclusionRepository conclusionRepository) {
        this.conclusionRepository = conclusionRepository;
    }

    /**
     * Return a {@link List} of {@link Conclusion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Conclusion> findByCriteria(ConclusionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Conclusion> specification = createSpecification(criteria);
        return conclusionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Conclusion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Conclusion> findByCriteria(ConclusionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Conclusion> specification = createSpecification(criteria);
        return conclusionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConclusionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Conclusion> specification = createSpecification(criteria);
        return conclusionRepository.count(specification);
    }

    /**
     * Function to convert {@link ConclusionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Conclusion> createSpecification(ConclusionCriteria criteria) {
        Specification<Conclusion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Conclusion_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Conclusion_.date));
            }
            if (criteria.getResultDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResultDescription(), Conclusion_.resultDescription));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Conclusion_.url));
            }
            if (criteria.getSignedById() != null) {
                specification = specification.and(buildSpecification(criteria.getSignedById(),
                    root -> root.join(Conclusion_.signedBy, JoinType.LEFT).get(Oncologist_.id)));
            }
            if (criteria.getForPatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getForPatientId(),
                    root -> root.join(Conclusion_.forPatient, JoinType.LEFT).get(Patient_.id)));
            }
        }
        return specification;
    }
}
