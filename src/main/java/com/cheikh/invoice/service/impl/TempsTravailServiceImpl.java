package com.cheikh.invoice.service.impl;

import com.cheikh.invoice.service.TempsTravailService;
import com.cheikh.invoice.domain.TempsTravail;
import com.cheikh.invoice.repository.TempsTravailRepository;
import com.cheikh.invoice.service.dto.TempsTravailDTO;
import com.cheikh.invoice.service.mapper.TempsTravailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TempsTravail}.
 */
@Service
@Transactional
public class TempsTravailServiceImpl implements TempsTravailService {

    private final Logger log = LoggerFactory.getLogger(TempsTravailServiceImpl.class);

    private final TempsTravailRepository tempsTravailRepository;

    private final TempsTravailMapper tempsTravailMapper;

    public TempsTravailServiceImpl(TempsTravailRepository tempsTravailRepository, TempsTravailMapper tempsTravailMapper) {
        this.tempsTravailRepository = tempsTravailRepository;
        this.tempsTravailMapper = tempsTravailMapper;
    }

    /**
     * Save a tempsTravail.
     *
     * @param tempsTravailDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TempsTravailDTO save(TempsTravailDTO tempsTravailDTO) {
        log.debug("Request to save TempsTravail : {}", tempsTravailDTO);
        TempsTravail tempsTravail = tempsTravailMapper.toEntity(tempsTravailDTO);
        tempsTravail = tempsTravailRepository.save(tempsTravail);
        return tempsTravailMapper.toDto(tempsTravail);
    }

    /**
     * Get all the tempsTravails.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TempsTravailDTO> findAll() {
        log.debug("Request to get all TempsTravails");
        return tempsTravailRepository.findAll().stream()
            .map(tempsTravailMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one tempsTravail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TempsTravailDTO> findOne(Long id) {
        log.debug("Request to get TempsTravail : {}", id);
        return tempsTravailRepository.findById(id)
            .map(tempsTravailMapper::toDto);
    }

    /**
     * Delete the tempsTravail by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TempsTravail : {}", id);
        tempsTravailRepository.deleteById(id);
    }
}
