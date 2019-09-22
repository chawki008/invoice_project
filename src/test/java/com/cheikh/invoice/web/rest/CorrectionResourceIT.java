package com.cheikh.invoice.web.rest;

import com.cheikh.invoice.InvoiceProjectApp;
import com.cheikh.invoice.domain.Correction;
import com.cheikh.invoice.domain.User;
import com.cheikh.invoice.domain.Facture;
import com.cheikh.invoice.repository.CorrectionRepository;
import com.cheikh.invoice.service.CorrectionService;
import com.cheikh.invoice.service.dto.CorrectionDTO;
import com.cheikh.invoice.service.mapper.CorrectionMapper;
import com.cheikh.invoice.web.rest.errors.ExceptionTranslator;
import com.cheikh.invoice.service.dto.CorrectionCriteria;
import com.cheikh.invoice.service.CorrectionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cheikh.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CorrectionResource} REST controller.
 */
@SpringBootTest(classes = InvoiceProjectApp.class)
public class CorrectionResourceIT {

    private static final String DEFAULT_CHAMP = "AAAAAAAAAA";
    private static final String UPDATED_CHAMP = "BBBBBBBBBB";

    private static final String DEFAULT_OLD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OLD_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private CorrectionMapper correctionMapper;

    @Autowired
    private CorrectionService correctionService;

    @Autowired
    private CorrectionQueryService correctionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCorrectionMockMvc;

    private Correction correction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorrectionResource correctionResource = new CorrectionResource(correctionService, correctionQueryService);
        this.restCorrectionMockMvc = MockMvcBuilders.standaloneSetup(correctionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correction createEntity(EntityManager em) {
        Correction correction = new Correction()
            .champ(DEFAULT_CHAMP)
            .oldValue(DEFAULT_OLD_VALUE)
            .newValue(DEFAULT_NEW_VALUE)
            .etat(DEFAULT_ETAT);
        return correction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correction createUpdatedEntity(EntityManager em) {
        Correction correction = new Correction()
            .champ(UPDATED_CHAMP)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .etat(UPDATED_ETAT);
        return correction;
    }

    @BeforeEach
    public void initTest() {
        correction = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorrection() throws Exception {
        int databaseSizeBeforeCreate = correctionRepository.findAll().size();

        // Create the Correction
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);
        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isCreated());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeCreate + 1);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getChamp()).isEqualTo(DEFAULT_CHAMP);
        assertThat(testCorrection.getOldValue()).isEqualTo(DEFAULT_OLD_VALUE);
        assertThat(testCorrection.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
        assertThat(testCorrection.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    public void createCorrectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = correctionRepository.findAll().size();

        // Create the Correction with an existing ID
        correction.setId(1L);
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCorrections() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList
        restCorrectionMockMvc.perform(get("/api/corrections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correction.getId().intValue())))
            .andExpect(jsonPath("$.[*].champ").value(hasItem(DEFAULT_CHAMP.toString())))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE.toString())))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }
    
    @Test
    @Transactional
    public void getCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get the correction
        restCorrectionMockMvc.perform(get("/api/corrections/{id}", correction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(correction.getId().intValue()))
            .andExpect(jsonPath("$.champ").value(DEFAULT_CHAMP.toString()))
            .andExpect(jsonPath("$.oldValue").value(DEFAULT_OLD_VALUE.toString()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    public void getAllCorrectionsByChampIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where champ equals to DEFAULT_CHAMP
        defaultCorrectionShouldBeFound("champ.equals=" + DEFAULT_CHAMP);

        // Get all the correctionList where champ equals to UPDATED_CHAMP
        defaultCorrectionShouldNotBeFound("champ.equals=" + UPDATED_CHAMP);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByChampIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where champ in DEFAULT_CHAMP or UPDATED_CHAMP
        defaultCorrectionShouldBeFound("champ.in=" + DEFAULT_CHAMP + "," + UPDATED_CHAMP);

        // Get all the correctionList where champ equals to UPDATED_CHAMP
        defaultCorrectionShouldNotBeFound("champ.in=" + UPDATED_CHAMP);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByChampIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where champ is not null
        defaultCorrectionShouldBeFound("champ.specified=true");

        // Get all the correctionList where champ is null
        defaultCorrectionShouldNotBeFound("champ.specified=false");
    }

    @Test
    @Transactional
    public void getAllCorrectionsByOldValueIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where oldValue equals to DEFAULT_OLD_VALUE
        defaultCorrectionShouldBeFound("oldValue.equals=" + DEFAULT_OLD_VALUE);

        // Get all the correctionList where oldValue equals to UPDATED_OLD_VALUE
        defaultCorrectionShouldNotBeFound("oldValue.equals=" + UPDATED_OLD_VALUE);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByOldValueIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where oldValue in DEFAULT_OLD_VALUE or UPDATED_OLD_VALUE
        defaultCorrectionShouldBeFound("oldValue.in=" + DEFAULT_OLD_VALUE + "," + UPDATED_OLD_VALUE);

        // Get all the correctionList where oldValue equals to UPDATED_OLD_VALUE
        defaultCorrectionShouldNotBeFound("oldValue.in=" + UPDATED_OLD_VALUE);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByOldValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where oldValue is not null
        defaultCorrectionShouldBeFound("oldValue.specified=true");

        // Get all the correctionList where oldValue is null
        defaultCorrectionShouldNotBeFound("oldValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllCorrectionsByNewValueIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where newValue equals to DEFAULT_NEW_VALUE
        defaultCorrectionShouldBeFound("newValue.equals=" + DEFAULT_NEW_VALUE);

        // Get all the correctionList where newValue equals to UPDATED_NEW_VALUE
        defaultCorrectionShouldNotBeFound("newValue.equals=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByNewValueIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where newValue in DEFAULT_NEW_VALUE or UPDATED_NEW_VALUE
        defaultCorrectionShouldBeFound("newValue.in=" + DEFAULT_NEW_VALUE + "," + UPDATED_NEW_VALUE);

        // Get all the correctionList where newValue equals to UPDATED_NEW_VALUE
        defaultCorrectionShouldNotBeFound("newValue.in=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByNewValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where newValue is not null
        defaultCorrectionShouldBeFound("newValue.specified=true");

        // Get all the correctionList where newValue is null
        defaultCorrectionShouldNotBeFound("newValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllCorrectionsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where etat equals to DEFAULT_ETAT
        defaultCorrectionShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the correctionList where etat equals to UPDATED_ETAT
        defaultCorrectionShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultCorrectionShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the correctionList where etat equals to UPDATED_ETAT
        defaultCorrectionShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where etat is not null
        defaultCorrectionShouldBeFound("etat.specified=true");

        // Get all the correctionList where etat is null
        defaultCorrectionShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    public void getAllCorrectionsBySasisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);
        User sasisseur = UserResourceIT.createEntity(em);
        em.persist(sasisseur);
        em.flush();
        correction.setSasisseur(sasisseur);
        correctionRepository.saveAndFlush(correction);
        Long sasisseurId = sasisseur.getId();

        // Get all the correctionList where sasisseur equals to sasisseurId
        defaultCorrectionShouldBeFound("sasisseurId.equals=" + sasisseurId);

        // Get all the correctionList where sasisseur equals to sasisseurId + 1
        defaultCorrectionShouldNotBeFound("sasisseurId.equals=" + (sasisseurId + 1));
    }


    @Test
    @Transactional
    public void getAllCorrectionsByVerificateurIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);
        User verificateur = UserResourceIT.createEntity(em);
        em.persist(verificateur);
        em.flush();
        correction.setVerificateur(verificateur);
        correctionRepository.saveAndFlush(correction);
        Long verificateurId = verificateur.getId();

        // Get all the correctionList where verificateur equals to verificateurId
        defaultCorrectionShouldBeFound("verificateurId.equals=" + verificateurId);

        // Get all the correctionList where verificateur equals to verificateurId + 1
        defaultCorrectionShouldNotBeFound("verificateurId.equals=" + (verificateurId + 1));
    }


    @Test
    @Transactional
    public void getAllCorrectionsByFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);
        Facture facture = FactureResourceIT.createEntity(em);
        em.persist(facture);
        em.flush();
        correction.setFacture(facture);
        correctionRepository.saveAndFlush(correction);
        Long factureId = facture.getId();

        // Get all the correctionList where facture equals to factureId
        defaultCorrectionShouldBeFound("factureId.equals=" + factureId);

        // Get all the correctionList where facture equals to factureId + 1
        defaultCorrectionShouldNotBeFound("factureId.equals=" + (factureId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorrectionShouldBeFound(String filter) throws Exception {
        restCorrectionMockMvc.perform(get("/api/corrections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correction.getId().intValue())))
            .andExpect(jsonPath("$.[*].champ").value(hasItem(DEFAULT_CHAMP)))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)));

        // Check, that the count call also returns 1
        restCorrectionMockMvc.perform(get("/api/corrections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorrectionShouldNotBeFound(String filter) throws Exception {
        restCorrectionMockMvc.perform(get("/api/corrections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorrectionMockMvc.perform(get("/api/corrections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCorrection() throws Exception {
        // Get the correction
        restCorrectionMockMvc.perform(get("/api/corrections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();

        // Update the correction
        Correction updatedCorrection = correctionRepository.findById(correction.getId()).get();
        // Disconnect from session so that the updates on updatedCorrection are not directly saved in db
        em.detach(updatedCorrection);
        updatedCorrection
            .champ(UPDATED_CHAMP)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .etat(UPDATED_ETAT);
        CorrectionDTO correctionDTO = correctionMapper.toDto(updatedCorrection);

        restCorrectionMockMvc.perform(put("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isOk());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getChamp()).isEqualTo(UPDATED_CHAMP);
        assertThat(testCorrection.getOldValue()).isEqualTo(UPDATED_OLD_VALUE);
        assertThat(testCorrection.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testCorrection.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void updateNonExistingCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();

        // Create the Correction
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrectionMockMvc.perform(put("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeDelete = correctionRepository.findAll().size();

        // Delete the correction
        restCorrectionMockMvc.perform(delete("/api/corrections/{id}", correction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Correction.class);
        Correction correction1 = new Correction();
        correction1.setId(1L);
        Correction correction2 = new Correction();
        correction2.setId(correction1.getId());
        assertThat(correction1).isEqualTo(correction2);
        correction2.setId(2L);
        assertThat(correction1).isNotEqualTo(correction2);
        correction1.setId(null);
        assertThat(correction1).isNotEqualTo(correction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrectionDTO.class);
        CorrectionDTO correctionDTO1 = new CorrectionDTO();
        correctionDTO1.setId(1L);
        CorrectionDTO correctionDTO2 = new CorrectionDTO();
        assertThat(correctionDTO1).isNotEqualTo(correctionDTO2);
        correctionDTO2.setId(correctionDTO1.getId());
        assertThat(correctionDTO1).isEqualTo(correctionDTO2);
        correctionDTO2.setId(2L);
        assertThat(correctionDTO1).isNotEqualTo(correctionDTO2);
        correctionDTO1.setId(null);
        assertThat(correctionDTO1).isNotEqualTo(correctionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(correctionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(correctionMapper.fromId(null)).isNull();
    }
}
