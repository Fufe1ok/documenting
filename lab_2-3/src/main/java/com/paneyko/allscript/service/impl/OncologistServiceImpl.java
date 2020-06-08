package com.paneyko.allscript.service.impl;

import com.paneyko.allscript.service.OncologistService;
import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.repository.OncologistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Oncologist}.
 */
@Service
@Transactional
public class OncologistServiceImpl implements OncologistService {

    private final Logger log = LoggerFactory.getLogger(OncologistServiceImpl.class);

    private final OncologistRepository oncologistRepository;

    public OncologistServiceImpl(OncologistRepository oncologistRepository) {
        this.oncologistRepository = oncologistRepository;
    }

    /**
     * Save a oncologist.
     *
     * @param oncologist the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Oncologist save(Oncologist oncologist) {
        log.debug("Request to save Oncologist : {}", oncologist);
        return oncologistRepository.save(oncologist);
    }

    /**
     * Get all the oncologists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Oncologist> findAll(Pageable pageable) {
        log.debug("Request to get all Oncologists");
        return oncologistRepository.findAll(pageable);
    }

    /**
     * Get one oncologist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Oncologist> findOne(Long id) {
        log.debug("Request to get Oncologist : {}", id);
        return oncologistRepository.findById(id);
    }

    /**
     * Delete the oncologist by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Oncologist : {}", id);
        oncologistRepository.deleteById(id);
    }
}
