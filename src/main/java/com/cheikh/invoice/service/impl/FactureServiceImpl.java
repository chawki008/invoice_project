package com.cheikh.invoice.service.impl;

import com.cheikh.invoice.service.FactureService;
import com.cheikh.invoice.domain.Facture;
import com.cheikh.invoice.repository.FactureRepository;
import com.cheikh.invoice.service.dto.FactureDTO;
import com.cheikh.invoice.service.mapper.FactureLazyMapper;
import com.cheikh.invoice.service.mapper.FactureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Facture}.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    private  FactureLazyMapper factureLazyMapper;

    public FactureServiceImpl(FactureRepository factureRepository, FactureMapper factureMapper, FactureLazyMapper factureLazyMapper) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
        this.factureLazyMapper = factureLazyMapper;
    }

    /**
     * Save a facture.
     *
     * @param factureDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FactureDTO save(FactureDTO factureDTO) {
        log.debug("Request to save Facture : {}", factureDTO);
        Facture facture = factureMapper.toEntity(factureDTO);
        facture = factureRepository.save(facture);
        return factureMapper.toDto(facture);
    }

    /**
     * Get all the factures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FactureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factures");
        return factureRepository.findAll(pageable)
            .map(factureLazyMapper::toDto);
    }

    /**
     * Get all the factures with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FactureDTO> findAllWithEagerRelationships(Pageable pageable) {
        return factureRepository.findAllWithEagerRelationships(pageable).map(factureMapper::toDto);
    }


    /**
     * Get one facture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FactureDTO> findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findOneWithEagerRelationships(id)
            .map(factureMapper::toDto);
    }

    @Override
    public List<FactureDTO> findAllByEtat(String etat) {
        return factureRepository.findAllByEtat(etat).stream().map(factureLazyMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Delete the facture by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
    }
}
