package com.paneyko.allscript.service.impl;

import com.paneyko.allscript.service.ConclusionService;
import com.paneyko.allscript.domain.Conclusion;
import com.paneyko.allscript.repository.ConclusionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Conclusion}.
 */
@Service
@Transactional
public class ConclusionServiceImpl implements ConclusionService {

    private final Logger log = LoggerFactory.getLogger(ConclusionServiceImpl.class);

    private final ConclusionRepository conclusionRepository;

    public ConclusionServiceImpl(ConclusionRepository conclusionRepository) {
        this.conclusionRepository = conclusionRepository;
    }

    /**
     * Save a conclusion.
     *
     * @param conclusion the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Conclusion save(Conclusion conclusion) {
        log.debug("Request to save Conclusion : {}", conclusion);
        return conclusionRepository.save(conclusion);
    }

    /**
     * Get all the conclusions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Conclusion> findAll(Pageable pageable) {
        log.debug("Request to get all Conclusions");
        return conclusionRepository.findAll(pageable);
    }

    /**
     * Get one conclusion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Conclusion> findOne(Long id) {
        log.debug("Request to get Conclusion : {}", id);
        return conclusionRepository.findById(id);
    }

    /**
     * Delete the conclusion by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Conclusion : {}", id);
        conclusionRepository.deleteById(id);
    }
}
