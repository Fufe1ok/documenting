package com.paneyko.allscript.service;

import com.paneyko.allscript.domain.Therapist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Therapist}.
 */
public interface TherapistService {

    /**
     * Save a therapist.
     *
     * @param therapist the entity to save.
     * @return the persisted entity.
     */
    Therapist save(Therapist therapist);

    /**
     * Get all the therapists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Therapist> findAll(Pageable pageable);

    /**
     * Get the "id" therapist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Therapist> findOne(Long id);

    /**
     * Delete the "id" therapist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
