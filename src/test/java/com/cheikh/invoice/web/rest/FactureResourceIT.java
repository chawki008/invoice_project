package com.cheikh.invoice.web.rest;

import com.cheikh.invoice.InvoiceProjectApp;
import com.cheikh.invoice.domain.Facture;
import com.cheikh.invoice.domain.User;
import com.cheikh.invoice.domain.Correction;
import com.cheikh.invoice.repository.FactureRepository;
import com.cheikh.invoice.service.FactureService;
import com.cheikh.invoice.service.UserService;
import com.cheikh.invoice.service.dto.FactureDTO;
import com.cheikh.invoice.service.mapper.FactureMapper;
import com.cheikh.invoice.web.rest.errors.ExceptionTranslator;
import com.cheikh.invoice.service.FactureQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
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
    private UserService userService;

    @Autowired
    private FactureQueryService factureQueryService;

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
        final FactureResource factureResource = new FactureResource(factureService, userService, factureQueryService);
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
        FactureResource factureResource = new FactureResource(factureServiceMock, userService, factureQueryService);
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
        FactureResource factureResource = new FactureResource(factureServiceMock, userService, factureQueryService);
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
    public void getAllFacturesByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where etat equals to DEFAULT_ETAT
        defaultFactureShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the factureList where etat equals to UPDATED_ETAT
        defaultFactureShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllFacturesByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultFactureShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the factureList where etat equals to UPDATED_ETAT
        defaultFactureShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllFacturesByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where etat is not null
        defaultFactureShouldBeFound("etat.specified=true");

        // Get all the factureList where etat is null
        defaultFactureShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where type equals to DEFAULT_TYPE
        defaultFactureShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the factureList where type equals to UPDATED_TYPE
        defaultFactureShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFacturesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultFactureShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the factureList where type equals to UPDATED_TYPE
        defaultFactureShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFacturesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where type is not null
        defaultFactureShouldBeFound("type.specified=true");

        // Get all the factureList where type is null
        defaultFactureShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where createdAt equals to DEFAULT_CREATED_AT
        defaultFactureShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the factureList where createdAt equals to UPDATED_CREATED_AT
        defaultFactureShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllFacturesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultFactureShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the factureList where createdAt equals to UPDATED_CREATED_AT
        defaultFactureShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllFacturesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where createdAt is not null
        defaultFactureShouldBeFound("createdAt.specified=true");

        // Get all the factureList where createdAt is null
        defaultFactureShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByLastModifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lastModifiedAt equals to DEFAULT_LAST_MODIFIED_AT
        defaultFactureShouldBeFound("lastModifiedAt.equals=" + DEFAULT_LAST_MODIFIED_AT);

        // Get all the factureList where lastModifiedAt equals to UPDATED_LAST_MODIFIED_AT
        defaultFactureShouldNotBeFound("lastModifiedAt.equals=" + UPDATED_LAST_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void getAllFacturesByLastModifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lastModifiedAt in DEFAULT_LAST_MODIFIED_AT or UPDATED_LAST_MODIFIED_AT
        defaultFactureShouldBeFound("lastModifiedAt.in=" + DEFAULT_LAST_MODIFIED_AT + "," + UPDATED_LAST_MODIFIED_AT);

        // Get all the factureList where lastModifiedAt equals to UPDATED_LAST_MODIFIED_AT
        defaultFactureShouldNotBeFound("lastModifiedAt.in=" + UPDATED_LAST_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void getAllFacturesByLastModifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lastModifiedAt is not null
        defaultFactureShouldBeFound("lastModifiedAt.specified=true");

        // Get all the factureList where lastModifiedAt is null
        defaultFactureShouldNotBeFound("lastModifiedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date equals to DEFAULT_DATE
        defaultFactureShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the factureList where date equals to UPDATED_DATE
        defaultFactureShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date in DEFAULT_DATE or UPDATED_DATE
        defaultFactureShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the factureList where date equals to UPDATED_DATE
        defaultFactureShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is not null
        defaultFactureShouldBeFound("date.specified=true");

        // Get all the factureList where date is null
        defaultFactureShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is greater than or equal to DEFAULT_DATE
        defaultFactureShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the factureList where date is greater than or equal to UPDATED_DATE
        defaultFactureShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is less than or equal to DEFAULT_DATE
        defaultFactureShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the factureList where date is less than or equal to SMALLER_DATE
        defaultFactureShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is less than DEFAULT_DATE
        defaultFactureShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the factureList where date is less than UPDATED_DATE
        defaultFactureShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is greater than DEFAULT_DATE
        defaultFactureShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the factureList where date is greater than SMALLER_DATE
        defaultFactureShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllFacturesByInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where info equals to DEFAULT_INFO
        defaultFactureShouldBeFound("info.equals=" + DEFAULT_INFO);

        // Get all the factureList where info equals to UPDATED_INFO
        defaultFactureShouldNotBeFound("info.equals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllFacturesByInfoIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where info in DEFAULT_INFO or UPDATED_INFO
        defaultFactureShouldBeFound("info.in=" + DEFAULT_INFO + "," + UPDATED_INFO);

        // Get all the factureList where info equals to UPDATED_INFO
        defaultFactureShouldNotBeFound("info.in=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllFacturesByInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where info is not null
        defaultFactureShouldBeFound("info.specified=true");

        // Get all the factureList where info is null
        defaultFactureShouldNotBeFound("info.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero equals to DEFAULT_NUMERO
        defaultFactureShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the factureList where numero equals to UPDATED_NUMERO
        defaultFactureShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllFacturesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultFactureShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the factureList where numero equals to UPDATED_NUMERO
        defaultFactureShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllFacturesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero is not null
        defaultFactureShouldBeFound("numero.specified=true");

        // Get all the factureList where numero is null
        defaultFactureShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantTTCIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTTC equals to DEFAULT_MONTANT_TTC
        defaultFactureShouldBeFound("montantTTC.equals=" + DEFAULT_MONTANT_TTC);

        // Get all the factureList where montantTTC equals to UPDATED_MONTANT_TTC
        defaultFactureShouldNotBeFound("montantTTC.equals=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantTTCIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTTC in DEFAULT_MONTANT_TTC or UPDATED_MONTANT_TTC
        defaultFactureShouldBeFound("montantTTC.in=" + DEFAULT_MONTANT_TTC + "," + UPDATED_MONTANT_TTC);

        // Get all the factureList where montantTTC equals to UPDATED_MONTANT_TTC
        defaultFactureShouldNotBeFound("montantTTC.in=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantTTCIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTTC is not null
        defaultFactureShouldBeFound("montantTTC.specified=true");

        // Get all the factureList where montantTTC is null
        defaultFactureShouldNotBeFound("montantTTC.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantTTCIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTTC is greater than or equal to DEFAULT_MONTANT_TTC
        defaultFactureShouldBeFound("montantTTC.greaterThanOrEqual=" + DEFAULT_MONTANT_TTC);

        // Get all the factureList where montantTTC is greater than or equal to UPDATED_MONTANT_TTC
        defaultFactureShouldNotBeFound("montantTTC.greaterThanOrEqual=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantTTCIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTTC is less than or equal to DEFAULT_MONTANT_TTC
        defaultFactureShouldBeFound("montantTTC.lessThanOrEqual=" + DEFAULT_MONTANT_TTC);

        // Get all the factureList where montantTTC is less than or equal to SMALLER_MONTANT_TTC
        defaultFactureShouldNotBeFound("montantTTC.lessThanOrEqual=" + SMALLER_MONTANT_TTC);
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantTTCIsLessThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTTC is less than DEFAULT_MONTANT_TTC
        defaultFactureShouldNotBeFound("montantTTC.lessThan=" + DEFAULT_MONTANT_TTC);

        // Get all the factureList where montantTTC is less than UPDATED_MONTANT_TTC
        defaultFactureShouldBeFound("montantTTC.lessThan=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantTTCIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTTC is greater than DEFAULT_MONTANT_TTC
        defaultFactureShouldNotBeFound("montantTTC.greaterThan=" + DEFAULT_MONTANT_TTC);

        // Get all the factureList where montantTTC is greater than SMALLER_MONTANT_TTC
        defaultFactureShouldBeFound("montantTTC.greaterThan=" + SMALLER_MONTANT_TTC);
    }


    @Test
    @Transactional
    public void getAllFacturesByFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where fournisseur equals to DEFAULT_FOURNISSEUR
        defaultFactureShouldBeFound("fournisseur.equals=" + DEFAULT_FOURNISSEUR);

        // Get all the factureList where fournisseur equals to UPDATED_FOURNISSEUR
        defaultFactureShouldNotBeFound("fournisseur.equals=" + UPDATED_FOURNISSEUR);
    }

    @Test
    @Transactional
    public void getAllFacturesByFournisseurIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where fournisseur in DEFAULT_FOURNISSEUR or UPDATED_FOURNISSEUR
        defaultFactureShouldBeFound("fournisseur.in=" + DEFAULT_FOURNISSEUR + "," + UPDATED_FOURNISSEUR);

        // Get all the factureList where fournisseur equals to UPDATED_FOURNISSEUR
        defaultFactureShouldNotBeFound("fournisseur.in=" + UPDATED_FOURNISSEUR);
    }

    @Test
    @Transactional
    public void getAllFacturesByFournisseurIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where fournisseur is not null
        defaultFactureShouldBeFound("fournisseur.specified=true");

        // Get all the factureList where fournisseur is null
        defaultFactureShouldNotBeFound("fournisseur.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByEcoTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where ecoTax equals to DEFAULT_ECO_TAX
        defaultFactureShouldBeFound("ecoTax.equals=" + DEFAULT_ECO_TAX);

        // Get all the factureList where ecoTax equals to UPDATED_ECO_TAX
        defaultFactureShouldNotBeFound("ecoTax.equals=" + UPDATED_ECO_TAX);
    }

    @Test
    @Transactional
    public void getAllFacturesByEcoTaxIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where ecoTax in DEFAULT_ECO_TAX or UPDATED_ECO_TAX
        defaultFactureShouldBeFound("ecoTax.in=" + DEFAULT_ECO_TAX + "," + UPDATED_ECO_TAX);

        // Get all the factureList where ecoTax equals to UPDATED_ECO_TAX
        defaultFactureShouldNotBeFound("ecoTax.in=" + UPDATED_ECO_TAX);
    }

    @Test
    @Transactional
    public void getAllFacturesByEcoTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where ecoTax is not null
        defaultFactureShouldBeFound("ecoTax.specified=true");

        // Get all the factureList where ecoTax is null
        defaultFactureShouldNotBeFound("ecoTax.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByEcoTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where ecoTax is greater than or equal to DEFAULT_ECO_TAX
        defaultFactureShouldBeFound("ecoTax.greaterThanOrEqual=" + DEFAULT_ECO_TAX);

        // Get all the factureList where ecoTax is greater than or equal to UPDATED_ECO_TAX
        defaultFactureShouldNotBeFound("ecoTax.greaterThanOrEqual=" + UPDATED_ECO_TAX);
    }

    @Test
    @Transactional
    public void getAllFacturesByEcoTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where ecoTax is less than or equal to DEFAULT_ECO_TAX
        defaultFactureShouldBeFound("ecoTax.lessThanOrEqual=" + DEFAULT_ECO_TAX);

        // Get all the factureList where ecoTax is less than or equal to SMALLER_ECO_TAX
        defaultFactureShouldNotBeFound("ecoTax.lessThanOrEqual=" + SMALLER_ECO_TAX);
    }

    @Test
    @Transactional
    public void getAllFacturesByEcoTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where ecoTax is less than DEFAULT_ECO_TAX
        defaultFactureShouldNotBeFound("ecoTax.lessThan=" + DEFAULT_ECO_TAX);

        // Get all the factureList where ecoTax is less than UPDATED_ECO_TAX
        defaultFactureShouldBeFound("ecoTax.lessThan=" + UPDATED_ECO_TAX);
    }

    @Test
    @Transactional
    public void getAllFacturesByEcoTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where ecoTax is greater than DEFAULT_ECO_TAX
        defaultFactureShouldNotBeFound("ecoTax.greaterThan=" + DEFAULT_ECO_TAX);

        // Get all the factureList where ecoTax is greater than SMALLER_ECO_TAX
        defaultFactureShouldBeFound("ecoTax.greaterThan=" + SMALLER_ECO_TAX);
    }


    @Test
    @Transactional
    public void getAllFacturesBySasisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        User sasisseur = UserResourceIT.createEntity(em);
        em.persist(sasisseur);
        em.flush();
        facture.setSasisseur(sasisseur);
        factureRepository.saveAndFlush(facture);
        Long sasisseurId = sasisseur.getId();

        // Get all the factureList where sasisseur equals to sasisseurId
        defaultFactureShouldBeFound("sasisseurId.equals=" + sasisseurId);

        // Get all the factureList where sasisseur equals to sasisseurId + 1
        defaultFactureShouldNotBeFound("sasisseurId.equals=" + (sasisseurId + 1));
    }


    @Test
    @Transactional
    public void getAllFacturesByVerificateurIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        User verificateur = UserResourceIT.createEntity(em);
        em.persist(verificateur);
        em.flush();
        facture.setVerificateur(verificateur);
        factureRepository.saveAndFlush(facture);
        Long verificateurId = verificateur.getId();

        // Get all the factureList where verificateur equals to verificateurId
        defaultFactureShouldBeFound("verificateurId.equals=" + verificateurId);

        // Get all the factureList where verificateur equals to verificateurId + 1
        defaultFactureShouldNotBeFound("verificateurId.equals=" + (verificateurId + 1));
    }


    @Test
    @Transactional
    public void getAllFacturesByCorrectionIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        Correction correction = CorrectionResourceIT.createEntity(em);
        em.persist(correction);
        em.flush();
        facture.addCorrection(correction);
        factureRepository.saveAndFlush(facture);
        Long correctionId = correction.getId();

        // Get all the factureList where correction equals to correctionId
        defaultFactureShouldBeFound("correctionId.equals=" + correctionId);

        // Get all the factureList where correction equals to correctionId + 1
        defaultFactureShouldNotBeFound("correctionId.equals=" + (correctionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactureShouldBeFound(String filter) throws Exception {
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedAt").value(hasItem(DEFAULT_LAST_MODIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].montantTTC").value(hasItem(DEFAULT_MONTANT_TTC)))
            .andExpect(jsonPath("$.[*].fournisseur").value(hasItem(DEFAULT_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].ecoTax").value(hasItem(DEFAULT_ECO_TAX)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restFactureMockMvc.perform(get("/api/factures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactureShouldNotBeFound(String filter) throws Exception {
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactureMockMvc.perform(get("/api/factures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
