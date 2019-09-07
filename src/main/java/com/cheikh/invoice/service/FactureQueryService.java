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

import com.cheikh.invoice.domain.Facture;
import com.cheikh.invoice.domain.*; // for static metamodels
import com.cheikh.invoice.repository.FactureRepository;
import com.cheikh.invoice.service.dto.FactureCriteria;
import com.cheikh.invoice.service.dto.FactureDTO;
import com.cheikh.invoice.service.mapper.FactureMapper;

/**
 * Service for executing complex queries for {@link Facture} entities in the database.
 * The main input is a {@link FactureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FactureDTO} or a {@link Page} of {@link FactureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactureQueryService extends QueryService<Facture> {

    private final Logger log = LoggerFactory.getLogger(FactureQueryService.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    public FactureQueryService(FactureRepository factureRepository, FactureMapper factureMapper) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
    }

    /**
     * Return a {@link List} of {@link FactureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FactureDTO> findByCriteria(FactureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureMapper.toDto(factureRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FactureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactureDTO> findByCriteria(FactureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.findAll(specification, page)
            .map(factureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Facture> createSpecification(FactureCriteria criteria) {
        Specification<Facture> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Facture_.id));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtat(), Facture_.etat));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Facture_.type));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Facture_.createdAt));
            }
            if (criteria.getLastModifiedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedAt(), Facture_.lastModifiedAt));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Facture_.date));
            }
            if (criteria.getInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfo(), Facture_.info));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Facture_.numero));
            }
            if (criteria.getMontantTTC() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantTTC(), Facture_.montantTTC));
            }
            if (criteria.getFournisseur() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFournisseur(), Facture_.fournisseur));
            }
            if (criteria.getEcoTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEcoTax(), Facture_.ecoTax));
            }
            if (criteria.getSasisseurId() != null) {
                specification = specification.and(buildSpecification(criteria.getSasisseurId(),
                    root -> root.join(Facture_.sasisseur, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getVerificateurId() != null) {
                specification = specification.and(buildSpecification(criteria.getVerificateurId(),
                    root -> root.join(Facture_.verificateur, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCorrectionId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrectionId(),
                    root -> root.join(Facture_.corrections, JoinType.LEFT).get(Correction_.id)));
            }
        }
        return specification;
    }
}
