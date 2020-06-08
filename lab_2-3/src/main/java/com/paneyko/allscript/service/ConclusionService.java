package com.paneyko.allscript.service;

import com.paneyko.allscript.domain.Conclusion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Conclusion}.
 */
public interface ConclusionService {

    /**
     * Save a conclusion.
     *
     * @param conclusion the entity to save.
     * @return the persisted entity.
     */
    Conclusion save(Conclusion conclusion);

    /**
     * Get all the conclusions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Conclusion> findAll(Pageable pageable);

    /**
     * Get the "id" conclusion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Conclusion> findOne(Long id);

    /**
     * Delete the "id" conclusion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
