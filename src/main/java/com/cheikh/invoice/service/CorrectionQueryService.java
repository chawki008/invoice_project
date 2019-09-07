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

import com.cheikh.invoice.domain.Correction;
import com.cheikh.invoice.domain.*; // for static metamodels
import com.cheikh.invoice.repository.CorrectionRepository;
import com.cheikh.invoice.service.dto.CorrectionCriteria;
import com.cheikh.invoice.service.dto.CorrectionDTO;
import com.cheikh.invoice.service.mapper.CorrectionMapper;

/**
 * Service for executing complex queries for {@link Correction} entities in the database.
 * The main input is a {@link CorrectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CorrectionDTO} or a {@link Page} of {@link CorrectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorrectionQueryService extends QueryService<Correction> {

    private final Logger log = LoggerFactory.getLogger(CorrectionQueryService.class);

    private final CorrectionRepository correctionRepository;

    private final CorrectionMapper correctionMapper;

    public CorrectionQueryService(CorrectionRepository correctionRepository, CorrectionMapper correctionMapper) {
        this.correctionRepository = correctionRepository;
        this.correctionMapper = correctionMapper;
    }

    /**
     * Return a {@link List} of {@link CorrectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CorrectionDTO> findByCriteria(CorrectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Correction> specification = createSpecification(criteria);
        return correctionMapper.toDto(correctionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CorrectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorrectionDTO> findByCriteria(CorrectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Correction> specification = createSpecification(criteria);
        return correctionRepository.findAll(specification, page)
            .map(correctionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorrectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Correction> specification = createSpecification(criteria);
        return correctionRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Correction> createSpecification(CorrectionCriteria criteria) {
        Specification<Correction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Correction_.id));
            }
            if (criteria.getChamp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChamp(), Correction_.champ));
            }
            if (criteria.getSasisseurId() != null) {
                specification = specification.and(buildSpecification(criteria.getSasisseurId(),
                    root -> root.join(Correction_.sasisseur, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getVerificateurId() != null) {
                specification = specification.and(buildSpecification(criteria.getVerificateurId(),
                    root -> root.join(Correction_.verificateur, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getFactureId() != null) {
                specification = specification.and(buildSpecification(criteria.getFactureId(),
                    root -> root.join(Correction_.factures, JoinType.LEFT).get(Facture_.id)));
            }
        }
        return specification;
    }
}
