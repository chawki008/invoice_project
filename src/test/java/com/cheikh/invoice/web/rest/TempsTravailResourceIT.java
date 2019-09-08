package com.cheikh.invoice.web.rest;

import com.cheikh.invoice.InvoiceProjectApp;
import com.cheikh.invoice.domain.TempsTravail;
import com.cheikh.invoice.domain.User;
import com.cheikh.invoice.repository.TempsTravailRepository;
import com.cheikh.invoice.service.TempsTravailService;
import com.cheikh.invoice.service.dto.TempsTravailDTO;
import com.cheikh.invoice.service.mapper.TempsTravailMapper;
import com.cheikh.invoice.web.rest.errors.ExceptionTranslator;
import com.cheikh.invoice.service.dto.TempsTravailCriteria;
import com.cheikh.invoice.service.TempsTravailQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.cheikh.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TempsTravailResource} REST controller.
 */
@SpringBootTest(classes = InvoiceProjectApp.class)
public class TempsTravailResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_START_DATE = Instant.ofEpochMilli(-1L);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_END_DATE = Instant.ofEpochMilli(-1L);

    @Autowired
    private TempsTravailRepository tempsTravailRepository;

    @Autowired
    private TempsTravailMapper tempsTravailMapper;

    @Autowired
    private TempsTravailService tempsTravailService;

    @Autowired
    private TempsTravailQueryService tempsTravailQueryService;

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

    private MockMvc restTempsTravailMockMvc;

    private TempsTravail tempsTravail;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TempsTravailResource tempsTravailResource = new TempsTravailResource(tempsTravailService, tempsTravailQueryService);
        this.restTempsTravailMockMvc = MockMvcBuilders.standaloneSetup(tempsTravailResource)
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
    public static TempsTravail createEntity(EntityManager em) {
        TempsTravail tempsTravail = new TempsTravail()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return tempsTravail;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TempsTravail createUpdatedEntity(EntityManager em) {
        TempsTravail tempsTravail = new TempsTravail()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return tempsTravail;
    }

    @BeforeEach
    public void initTest() {
        tempsTravail = createEntity(em);
    }

    @Test
    @Transactional
    public void createTempsTravail() throws Exception {
        int databaseSizeBeforeCreate = tempsTravailRepository.findAll().size();

        // Create the TempsTravail
        TempsTravailDTO tempsTravailDTO = tempsTravailMapper.toDto(tempsTravail);
        restTempsTravailMockMvc.perform(post("/api/temps-travails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempsTravailDTO)))
            .andExpect(status().isCreated());

        // Validate the TempsTravail in the database
        List<TempsTravail> tempsTravailList = tempsTravailRepository.findAll();
        assertThat(tempsTravailList).hasSize(databaseSizeBeforeCreate + 1);
        TempsTravail testTempsTravail = tempsTravailList.get(tempsTravailList.size() - 1);
        assertThat(testTempsTravail.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTempsTravail.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createTempsTravailWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tempsTravailRepository.findAll().size();

        // Create the TempsTravail with an existing ID
        tempsTravail.setId(1L);
        TempsTravailDTO tempsTravailDTO = tempsTravailMapper.toDto(tempsTravail);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTempsTravailMockMvc.perform(post("/api/temps-travails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempsTravailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TempsTravail in the database
        List<TempsTravail> tempsTravailList = tempsTravailRepository.findAll();
        assertThat(tempsTravailList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTempsTravails() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get all the tempsTravailList
        restTempsTravailMockMvc.perform(get("/api/temps-travails?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tempsTravail.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getTempsTravail() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get the tempsTravail
        restTempsTravailMockMvc.perform(get("/api/temps-travails/{id}", tempsTravail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tempsTravail.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllTempsTravailsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get all the tempsTravailList where startDate equals to DEFAULT_START_DATE
        defaultTempsTravailShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the tempsTravailList where startDate equals to UPDATED_START_DATE
        defaultTempsTravailShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTempsTravailsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get all the tempsTravailList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultTempsTravailShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the tempsTravailList where startDate equals to UPDATED_START_DATE
        defaultTempsTravailShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTempsTravailsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get all the tempsTravailList where startDate is not null
        defaultTempsTravailShouldBeFound("startDate.specified=true");

        // Get all the tempsTravailList where startDate is null
        defaultTempsTravailShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTempsTravailsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get all the tempsTravailList where endDate equals to DEFAULT_END_DATE
        defaultTempsTravailShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the tempsTravailList where endDate equals to UPDATED_END_DATE
        defaultTempsTravailShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTempsTravailsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get all the tempsTravailList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultTempsTravailShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the tempsTravailList where endDate equals to UPDATED_END_DATE
        defaultTempsTravailShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTempsTravailsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        // Get all the tempsTravailList where endDate is not null
        defaultTempsTravailShouldBeFound("endDate.specified=true");

        // Get all the tempsTravailList where endDate is null
        defaultTempsTravailShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTempsTravailsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tempsTravail.setUser(user);
        tempsTravailRepository.saveAndFlush(tempsTravail);
        Long userId = user.getId();

        // Get all the tempsTravailList where user equals to userId
        defaultTempsTravailShouldBeFound("userId.equals=" + userId);

        // Get all the tempsTravailList where user equals to userId + 1
        defaultTempsTravailShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTempsTravailShouldBeFound(String filter) throws Exception {
        restTempsTravailMockMvc.perform(get("/api/temps-travails?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tempsTravail.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restTempsTravailMockMvc.perform(get("/api/temps-travails/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTempsTravailShouldNotBeFound(String filter) throws Exception {
        restTempsTravailMockMvc.perform(get("/api/temps-travails?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTempsTravailMockMvc.perform(get("/api/temps-travails/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTempsTravail() throws Exception {
        // Get the tempsTravail
        restTempsTravailMockMvc.perform(get("/api/temps-travails/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTempsTravail() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        int databaseSizeBeforeUpdate = tempsTravailRepository.findAll().size();

        // Update the tempsTravail
        TempsTravail updatedTempsTravail = tempsTravailRepository.findById(tempsTravail.getId()).get();
        // Disconnect from session so that the updates on updatedTempsTravail are not directly saved in db
        em.detach(updatedTempsTravail);
        updatedTempsTravail
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        TempsTravailDTO tempsTravailDTO = tempsTravailMapper.toDto(updatedTempsTravail);

        restTempsTravailMockMvc.perform(put("/api/temps-travails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempsTravailDTO)))
            .andExpect(status().isOk());

        // Validate the TempsTravail in the database
        List<TempsTravail> tempsTravailList = tempsTravailRepository.findAll();
        assertThat(tempsTravailList).hasSize(databaseSizeBeforeUpdate);
        TempsTravail testTempsTravail = tempsTravailList.get(tempsTravailList.size() - 1);
        assertThat(testTempsTravail.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTempsTravail.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTempsTravail() throws Exception {
        int databaseSizeBeforeUpdate = tempsTravailRepository.findAll().size();

        // Create the TempsTravail
        TempsTravailDTO tempsTravailDTO = tempsTravailMapper.toDto(tempsTravail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTempsTravailMockMvc.perform(put("/api/temps-travails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempsTravailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TempsTravail in the database
        List<TempsTravail> tempsTravailList = tempsTravailRepository.findAll();
        assertThat(tempsTravailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTempsTravail() throws Exception {
        // Initialize the database
        tempsTravailRepository.saveAndFlush(tempsTravail);

        int databaseSizeBeforeDelete = tempsTravailRepository.findAll().size();

        // Delete the tempsTravail
        restTempsTravailMockMvc.perform(delete("/api/temps-travails/{id}", tempsTravail.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TempsTravail> tempsTravailList = tempsTravailRepository.findAll();
        assertThat(tempsTravailList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TempsTravail.class);
        TempsTravail tempsTravail1 = new TempsTravail();
        tempsTravail1.setId(1L);
        TempsTravail tempsTravail2 = new TempsTravail();
        tempsTravail2.setId(tempsTravail1.getId());
        assertThat(tempsTravail1).isEqualTo(tempsTravail2);
        tempsTravail2.setId(2L);
        assertThat(tempsTravail1).isNotEqualTo(tempsTravail2);
        tempsTravail1.setId(null);
        assertThat(tempsTravail1).isNotEqualTo(tempsTravail2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TempsTravailDTO.class);
        TempsTravailDTO tempsTravailDTO1 = new TempsTravailDTO();
        tempsTravailDTO1.setId(1L);
        TempsTravailDTO tempsTravailDTO2 = new TempsTravailDTO();
        assertThat(tempsTravailDTO1).isNotEqualTo(tempsTravailDTO2);
        tempsTravailDTO2.setId(tempsTravailDTO1.getId());
        assertThat(tempsTravailDTO1).isEqualTo(tempsTravailDTO2);
        tempsTravailDTO2.setId(2L);
        assertThat(tempsTravailDTO1).isNotEqualTo(tempsTravailDTO2);
        tempsTravailDTO1.setId(null);
        assertThat(tempsTravailDTO1).isNotEqualTo(tempsTravailDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tempsTravailMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tempsTravailMapper.fromId(null)).isNull();
    }
}
