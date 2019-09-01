package com.cheikh.invoice.service;

import com.cheikh.invoice.service.dto.TempsTravailDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cheikh.invoice.domain.TempsTravail}.
 */
public interface TempsTravailService {

    /**
     * Save a tempsTravail.
     *
     * @param tempsTravailDTO the entity to save.
     * @return the persisted entity.
     */
    TempsTravailDTO save(TempsTravailDTO tempsTravailDTO);

    /**
     * Get all the tempsTravails.
     *
     * @return the list of entities.
     */
    List<TempsTravailDTO> findAll();


    /**
     * Get the "id" tempsTravail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TempsTravailDTO> findOne(Long id);

    /**
     * Delete the "id" tempsTravail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
