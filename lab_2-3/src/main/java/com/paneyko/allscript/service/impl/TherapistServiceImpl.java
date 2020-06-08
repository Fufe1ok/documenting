package com.paneyko.allscript.service.impl;

import com.paneyko.allscript.service.TherapistService;
import com.paneyko.allscript.domain.Therapist;
import com.paneyko.allscript.repository.TherapistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Therapist}.
 */
@Service
@Transactional
public class TherapistServiceImpl implements TherapistService {

    private final Logger log = LoggerFactory.getLogger(TherapistServiceImpl.class);

    private final TherapistRepository therapistRepository;

    public TherapistServiceImpl(TherapistRepository therapistRepository) {
        this.therapistRepository = therapistRepository;
    }

    /**
     * Save a therapist.
     *
     * @param therapist the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Therapist save(Therapist therapist) {
        log.debug("Request to save Therapist : {}", therapist);
        return therapistRepository.save(therapist);
    }

    /**
     * Get all the therapists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Therapist> findAll(Pageable pageable) {
        log.debug("Request to get all Therapists");
        return therapistRepository.findAll(pageable);
    }

    /**
     * Get one therapist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Therapist> findOne(Long id) {
        log.debug("Request to get Therapist : {}", id);
        return therapistRepository.findById(id);
    }

    /**
     * Delete the therapist by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Therapist : {}", id);
        therapistRepository.deleteById(id);
    }
}
