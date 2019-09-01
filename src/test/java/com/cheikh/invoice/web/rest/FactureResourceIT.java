package com.cheikh.invoice.web.rest;

import com.cheikh.invoice.InvoiceProjectApp;
import com.cheikh.invoice.domain.Facture;
import com.cheikh.invoice.repository.FactureRepository;
import com.cheikh.invoice.service.FactureService;
import com.cheikh.invoice.service.dto.FactureDTO;
import com.cheikh.invoice.service.mapper.FactureMapper;
import com.cheikh.invoice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.cheikh.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FactureResource} REST controller.
 */
@SpringBootTest(classes = InvoiceProjectApp.class)
public class FactureResourceIT {

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATED_AT = Instant.ofEpochMilli(-1L);

    private static final Instant DEFAULT_LAST_MODIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_LAST_MODIFIED_AT = Instant.ofEpochMilli(-1L);

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final Integer DEFAULT_MONTANT_TTC = 1;
    private static final Integer UPDATED_MONTANT_TTC = 2;
    private static final Integer SMALLER_MONTANT_TTC = 1 - 1;

    private static final String DEFAULT_FOURNISSEUR = "AAAAAAAAAA";
    private static final String UPDATED_FOURNISSEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_ECO_TAX = 1;
    private static final Integer UPDATED_ECO_TAX = 2;
    private static final Integer SMALLER_ECO_TAX = 1 - 1;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private FactureRepository factureRepository;

    @Mock
    private FactureRepository factureRepositoryMock;

    @Autowired
    private FactureMapper factureMapper;

    @Mock
    private FactureService factureServiceMock;

    @Autowired
    private FactureService factureService;

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

    private MockMvc restFactureMockMvc;

    private Facture facture;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FactureResource factureResource = new FactureResource(factureService);
        this.restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
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
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .etat(DEFAULT_ETAT)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .lastModifiedAt(DEFAULT_LAST_MODIFIED_AT)
            .date(DEFAULT_DATE)
            .info(DEFAULT_INFO)
            .numero(DEFAULT_NUMERO)
            .montantTTC(DEFAULT_MONTANT_TTC)
            .fournisseur(DEFAULT_FOURNISSEUR)
            .ecoTax(DEFAULT_ECO_TAX)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return facture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity(EntityManager em) {
        Facture facture = new Facture()
            .etat(UPDATED_ETAT)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .lastModifiedAt(UPDATED_LAST_MODIFIED_AT)
            .date(UPDATED_DATE)
            .info(UPDATED_INFO)
            .numero(UPDATED_NUMERO)
            .montantTTC(UPDATED_MONTANT_TTC)
            .fournisseur(UPDATED_FOURNISSEUR)
            .ecoTax(UPDATED_ECO_TAX)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return facture;
    }

    @BeforeEach
    public void initTest() {
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testFacture.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFacture.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFacture.getLastModifiedAt()).isEqualTo(DEFAULT_LAST_MODIFIED_AT);
        assertThat(testFacture.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFacture.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testFacture.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testFacture.getMontantTTC()).isEqualTo(DEFAULT_MONTANT_TTC);
        assertThat(testFacture.getFournisseur()).isEqualTo(DEFAULT_FOURNISSEUR);
        assertThat(testFacture.getEcoTax()).isEqualTo(DEFAULT_ECO_TAX);
        assertThat(testFacture.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFacture.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        facture.setId(1L);
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedAt").value(hasItem(DEFAULT_LAST_MODIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].montantTTC").value(hasItem(DEFAULT_MONTANT_TTC)))
            .andExpect(jsonPath("$.[*].fournisseur").value(hasItem(DEFAULT_FOURNISSEUR.toString())))
            .andExpect(jsonPath("$.[*].ecoTax").value(hasItem(DEFAULT_ECO_TAX)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFacturesWithEagerRelationshipsIsEnabled() throws Exception {
        FactureResource factureResource = new FactureResource(factureServiceMock);
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFactureMockMvc.perform(get("/api/factures?eagerload=true"))
        .andExpect(status().isOk());

        verify(factureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFacturesWithEagerRelationshipsIsNotEnabled() throws Exception {
        FactureResource factureResource = new FactureResource(factureServiceMock);
            when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFactureMockMvc.perform(get("/api/factures?eagerload=true"))
        .andExpect(status().isOk());

            verify(factureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.lastModifiedAt").value(DEFAULT_LAST_MODIFIED_AT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.montantTTC").value(DEFAULT_MONTANT_TTC))
            .andExpect(jsonPath("$.fournisseur").value(DEFAULT_FOURNISSEUR.toString()))
            .andExpect(jsonPath("$.ecoTax").value(DEFAULT_ECO_TAX))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).get();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .etat(UPDATED_ETAT)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .lastModifiedAt(UPDATED_LAST_MODIFIED_AT)
            .date(UPDATED_DATE)
            .info(UPDATED_INFO)
            .numero(UPDATED_NUMERO)
            .montantTTC(UPDATED_MONTANT_TTC)
            .fournisseur(UPDATED_FOURNISSEUR)
            .ecoTax(UPDATED_ECO_TAX)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        FactureDTO factureDTO = factureMapper.toDto(updatedFacture);

        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testFacture.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFacture.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFacture.getLastModifiedAt()).isEqualTo(UPDATED_LAST_MODIFIED_AT);
        assertThat(testFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFacture.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testFacture.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testFacture.getMontantTTC()).isEqualTo(UPDATED_MONTANT_TTC);
        assertThat(testFacture.getFournisseur()).isEqualTo(UPDATED_FOURNISSEUR);
        assertThat(testFacture.getEcoTax()).isEqualTo(UPDATED_ECO_TAX);
        assertThat(testFacture.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFacture.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Delete the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = new Facture();
        facture1.setId(1L);
        Facture facture2 = new Facture();
        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);
        facture2.setId(2L);
        assertThat(facture1).isNotEqualTo(facture2);
        facture1.setId(null);
        assertThat(facture1).isNotEqualTo(facture2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactureDTO.class);
        FactureDTO factureDTO1 = new FactureDTO();
        factureDTO1.setId(1L);
        FactureDTO factureDTO2 = new FactureDTO();
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
        factureDTO2.setId(factureDTO1.getId());
        assertThat(factureDTO1).isEqualTo(factureDTO2);
        factureDTO2.setId(2L);
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
        factureDTO1.setId(null);
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(factureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(factureMapper.fromId(null)).isNull();
    }
}
