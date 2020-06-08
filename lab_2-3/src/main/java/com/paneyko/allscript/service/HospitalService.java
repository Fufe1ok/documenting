package com.paneyko.allscript.service;

import com.paneyko.allscript.domain.Hospital;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Hospital}.
 */
public interface HospitalService {

    /**
     * Save a hospital.
     *
     * @param hospital the entity to save.
     * @return the persisted entity.
     */
    Hospital save(Hospital hospital);

    /**
     * Get all the hospitals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Hospital> findAll(Pageable pageable);

    /**
     * Get the "id" hospital.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Hospital> findOne(Long id);

    /**
     * Delete the "id" hospital.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
