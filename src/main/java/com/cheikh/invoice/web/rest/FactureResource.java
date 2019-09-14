package com.cheikh.invoice.web.rest;

import com.cheikh.invoice.service.FactureService;
import com.cheikh.invoice.web.rest.errors.BadRequestAlertException;
import com.cheikh.invoice.service.dto.FactureDTO;
import com.cheikh.invoice.service.dto.FactureCriteria;
import com.cheikh.invoice.service.FactureQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * REST controller for managing {@link com.cheikh.invoice.domain.Facture}.
 */
@RestController
@RequestMapping("/api")
public class FactureResource {

    private final Logger log = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactureService factureService;

    private final FactureQueryService factureQueryService;

    public FactureResource(FactureService factureService, FactureQueryService factureQueryService) {
        this.factureService = factureService;
        this.factureQueryService = factureQueryService;
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param factureDTO the factureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factureDTO, or with status {@code 400 (Bad Request)} if the facture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factures")
    public ResponseEntity<FactureDTO> createFacture(@RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", factureDTO);
        if (factureDTO.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factures} : Updates an existing facture.
     *
     * @param factureDTO the factureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factureDTO,
     * or with status {@code 400 (Bad Request)} if the factureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factures")
    public ResponseEntity<FactureDTO> updateFacture(@RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.debug("REST request to update Facture : {}", factureDTO);
        if (factureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /factures} : get all the factures.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factures in body.
     */
    @GetMapping("/factures")
    public ResponseEntity<List<FactureDTO>> getAllFactures(FactureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Factures by criteria: {}", criteria);
        Page<FactureDTO> page = factureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factures} : get all the factures.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factures in body.
     */
    @GetMapping("/factures/countByDate")
    public ResponseEntity<Map<LocalDate, Integer>> countFacturesByDate(FactureCriteria criteria) {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of("GMT+1");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);

        log.debug("REST request to get Factures by criteria: {}", criteria);
        List<FactureDTO> factures = factureQueryService.findByCriteria(criteria);
        Map<LocalDate, List<FactureDTO>> byDay = factures.stream().collect(groupingBy(facture -> LocalDateTime.ofInstant(facture.getLastModifiedAt(), zoneOffSet).toLocalDate()));
        Map<LocalDate, Integer> nbrFacturesParJour = byDay.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().size()));
        return ResponseEntity.ok().body(nbrFacturesParJour);
    }

    /**
    * {@code GET  /factures/count} : count all the factures.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/factures/count")
    public ResponseEntity<Long> countFactures(FactureCriteria criteria) {
        log.debug("REST request to count Factures by criteria: {}", criteria);
        return ResponseEntity.ok().body(factureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the factureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factures/{id}")
    public ResponseEntity<FactureDTO> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        Optional<FactureDTO> factureDTO = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factureDTO);
    }
    @GetMapping("/factures/vide")
    public ResponseEntity<FactureDTO> getFactureVide() {
        log.debug("REST request to get Facture vide");
        List<FactureDTO> facturesVide = factureService.findAllByEtat("VIDE");
        Optional<FactureDTO> factureDTO = facturesVide.isEmpty() ? Optional.empty() :Optional.of(facturesVide.get(0));
        if (factureDTO.isPresent()) {
            factureDTO.get().setEtat("EN_TRAIN_DE_SASIE");
            factureService.save(factureDTO.get());
        }
        return ResponseUtil.wrapOrNotFound(factureDTO);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the factureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
