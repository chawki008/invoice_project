package com.cheikh.invoice.web.rest;

import com.cheikh.invoice.service.TempsTravailService;
import com.cheikh.invoice.web.rest.errors.BadRequestAlertException;
import com.cheikh.invoice.service.dto.TempsTravailDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheikh.invoice.domain.TempsTravail}.
 */
@RestController
@RequestMapping("/api")
public class TempsTravailResource {

    private final Logger log = LoggerFactory.getLogger(TempsTravailResource.class);

    private static final String ENTITY_NAME = "tempsTravail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TempsTravailService tempsTravailService;

    public TempsTravailResource(TempsTravailService tempsTravailService) {
        this.tempsTravailService = tempsTravailService;
    }

    /**
     * {@code POST  /temps-travails} : Create a new tempsTravail.
     *
     * @param tempsTravailDTO the tempsTravailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tempsTravailDTO, or with status {@code 400 (Bad Request)} if the tempsTravail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temps-travails")
    public ResponseEntity<TempsTravailDTO> createTempsTravail(@RequestBody TempsTravailDTO tempsTravailDTO) throws URISyntaxException {
        log.debug("REST request to save TempsTravail : {}", tempsTravailDTO);
        if (tempsTravailDTO.getId() != null) {
            throw new BadRequestAlertException("A new tempsTravail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TempsTravailDTO result = tempsTravailService.save(tempsTravailDTO);
        return ResponseEntity.created(new URI("/api/temps-travails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temps-travails} : Updates an existing tempsTravail.
     *
     * @param tempsTravailDTO the tempsTravailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tempsTravailDTO,
     * or with status {@code 400 (Bad Request)} if the tempsTravailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tempsTravailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temps-travails")
    public ResponseEntity<TempsTravailDTO> updateTempsTravail(@RequestBody TempsTravailDTO tempsTravailDTO) throws URISyntaxException {
        log.debug("REST request to update TempsTravail : {}", tempsTravailDTO);
        if (tempsTravailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TempsTravailDTO result = tempsTravailService.save(tempsTravailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tempsTravailDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /temps-travails} : get all the tempsTravails.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tempsTravails in body.
     */
    @GetMapping("/temps-travails")
    public List<TempsTravailDTO> getAllTempsTravails() {
        log.debug("REST request to get all TempsTravails");
        return tempsTravailService.findAll();
    }

    /**
     * {@code GET  /temps-travails/:id} : get the "id" tempsTravail.
     *
     * @param id the id of the tempsTravailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tempsTravailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temps-travails/{id}")
    public ResponseEntity<TempsTravailDTO> getTempsTravail(@PathVariable Long id) {
        log.debug("REST request to get TempsTravail : {}", id);
        Optional<TempsTravailDTO> tempsTravailDTO = tempsTravailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tempsTravailDTO);
    }

    /**
     * {@code DELETE  /temps-travails/:id} : delete the "id" tempsTravail.
     *
     * @param id the id of the tempsTravailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temps-travails/{id}")
    public ResponseEntity<Void> deleteTempsTravail(@PathVariable Long id) {
        log.debug("REST request to delete TempsTravail : {}", id);
        tempsTravailService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
