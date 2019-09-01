package com.cheikh.invoice.service.impl;

import com.cheikh.invoice.service.CorrectionService;
import com.cheikh.invoice.domain.Correction;
import com.cheikh.invoice.repository.CorrectionRepository;
import com.cheikh.invoice.service.dto.CorrectionDTO;
import com.cheikh.invoice.service.mapper.CorrectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Correction}.
 */
@Service
@Transactional
public class CorrectionServiceImpl implements CorrectionService {

    private final Logger log = LoggerFactory.getLogger(CorrectionServiceImpl.class);

    private final CorrectionRepository correctionRepository;

    private final CorrectionMapper correctionMapper;

    public CorrectionServiceImpl(CorrectionRepository correctionRepository, CorrectionMapper correctionMapper) {
        this.correctionRepository = correctionRepository;
        this.correctionMapper = correctionMapper;
    }

    /**
     * Save a correction.
     *
     * @param correctionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CorrectionDTO save(CorrectionDTO correctionDTO) {
        log.debug("Request to save Correction : {}", correctionDTO);
        Correction correction = correctionMapper.toEntity(correctionDTO);
        correction = correctionRepository.save(correction);
        return correctionMapper.toDto(correction);
    }

    /**
     * Get all the corrections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CorrectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Corrections");
        return correctionRepository.findAll(pageable)
            .map(correctionMapper::toDto);
    }


    /**
     * Get one correction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CorrectionDTO> findOne(Long id) {
        log.debug("Request to get Correction : {}", id);
        return correctionRepository.findById(id)
            .map(correctionMapper::toDto);
    }

    /**
     * Delete the correction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Correction : {}", id);
        correctionRepository.deleteById(id);
    }
}
