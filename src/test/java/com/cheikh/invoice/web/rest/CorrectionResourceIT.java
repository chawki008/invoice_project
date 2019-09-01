package com.cheikh.invoice.web.rest;

import com.cheikh.invoice.InvoiceProjectApp;
import com.cheikh.invoice.domain.Correction;
import com.cheikh.invoice.repository.CorrectionRepository;
import com.cheikh.invoice.service.CorrectionService;
import com.cheikh.invoice.service.dto.CorrectionDTO;
import com.cheikh.invoice.service.mapper.CorrectionMapper;
import com.cheikh.invoice.web.rest.errors.ExceptionTranslator;

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

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private CorrectionMapper correctionMapper;

    @Autowired
    private CorrectionService correctionService;

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
        final CorrectionResource correctionResource = new CorrectionResource(correctionService);
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
            .champ(DEFAULT_CHAMP);
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
            .champ(UPDATED_CHAMP);
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
            .andExpect(jsonPath("$.[*].champ").value(hasItem(DEFAULT_CHAMP.toString())));
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
            .andExpect(jsonPath("$.champ").value(DEFAULT_CHAMP.toString()));
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
            .champ(UPDATED_CHAMP);
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
