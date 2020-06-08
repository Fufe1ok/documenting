package com.paneyko.allscript.service;

import com.paneyko.allscript.domain.Patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Patient}.
 */
public interface PatientService {

    /**
     * Save a patient.
     *
     * @param patient the entity to save.
     * @return the persisted entity.
     */
    Patient save(Patient patient);

    /**
     * Get all the patients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Patient> findAll(Pageable pageable);

    /**
     * Get all the patients with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Patient> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" patient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Patient> findOne(Long id);

    /**
     * Delete the "id" patient.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
