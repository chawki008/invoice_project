package com.cheikh.invoice.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.cheikh.invoice.domain.TempsTravail;
import com.cheikh.invoice.domain.*; // for static metamodels
import com.cheikh.invoice.repository.TempsTravailRepository;
import com.cheikh.invoice.service.dto.TempsTravailCriteria;
import com.cheikh.invoice.service.dto.TempsTravailDTO;
import com.cheikh.invoice.service.mapper.TempsTravailMapper;

/**
 * Service for executing complex queries for {@link TempsTravail} entities in the database.
 * The main input is a {@link TempsTravailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TempsTravailDTO} or a {@link Page} of {@link TempsTravailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TempsTravailQueryService extends QueryService<TempsTravail> {

    private final Logger log = LoggerFactory.getLogger(TempsTravailQueryService.class);

    private final TempsTravailRepository tempsTravailRepository;

    private final TempsTravailMapper tempsTravailMapper;

    public TempsTravailQueryService(TempsTravailRepository tempsTravailRepository, TempsTravailMapper tempsTravailMapper) {
        this.tempsTravailRepository = tempsTravailRepository;
        this.tempsTravailMapper = tempsTravailMapper;
    }

    /**
     * Return a {@link List} of {@link TempsTravailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TempsTravailDTO> findByCriteria(TempsTravailCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TempsTravail> specification = createSpecification(criteria);
        return tempsTravailMapper.toDto(tempsTravailRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TempsTravailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TempsTravailDTO> findByCriteria(TempsTravailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TempsTravail> specification = createSpecification(criteria);
        return tempsTravailRepository.findAll(specification, page)
            .map(tempsTravailMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TempsTravailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TempsTravail> specification = createSpecification(criteria);
        return tempsTravailRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<TempsTravail> createSpecification(TempsTravailCriteria criteria) {
        Specification<TempsTravail> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TempsTravail_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), TempsTravail_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), TempsTravail_.endDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(TempsTravail_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
