package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.AllscriptApp;
import com.paneyko.allscript.domain.Conclusion;
import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.domain.Patient;
import com.paneyko.allscript.repository.ConclusionRepository;
import com.paneyko.allscript.service.ConclusionService;
import com.paneyko.allscript.service.dto.ConclusionCriteria;
import com.paneyko.allscript.service.ConclusionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.paneyko.allscript.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ConclusionResource} REST controller.
 */
@SpringBootTest(classes = AllscriptApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ConclusionResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_RESULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private ConclusionRepository conclusionRepository;

    @Autowired
    private ConclusionService conclusionService;

    @Autowired
    private ConclusionQueryService conclusionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConclusionMockMvc;

    private Conclusion conclusion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conclusion createEntity(EntityManager em) {
        Conclusion conclusion = new Conclusion()
            .date(DEFAULT_DATE)
            .resultDescription(DEFAULT_RESULT_DESCRIPTION)
            .url(DEFAULT_URL);
        return conclusion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conclusion createUpdatedEntity(EntityManager em) {
        Conclusion conclusion = new Conclusion()
            .date(UPDATED_DATE)
            .resultDescription(UPDATED_RESULT_DESCRIPTION)
            .url(UPDATED_URL);
        return conclusion;
    }

    @BeforeEach
    public void initTest() {
        conclusion = createEntity(em);
    }

    @Test
    @Transactional
    public void createConclusion() throws Exception {
        int databaseSizeBeforeCreate = conclusionRepository.findAll().size();

        // Create the Conclusion
        restConclusionMockMvc.perform(post("/api/conclusions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(conclusion)))
            .andExpect(status().isCreated());

        // Validate the Conclusion in the database
        List<Conclusion> conclusionList = conclusionRepository.findAll();
        assertThat(conclusionList).hasSize(databaseSizeBeforeCreate + 1);
        Conclusion testConclusion = conclusionList.get(conclusionList.size() - 1);
        assertThat(testConclusion.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testConclusion.getResultDescription()).isEqualTo(DEFAULT_RESULT_DESCRIPTION);
        assertThat(testConclusion.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createConclusionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conclusionRepository.findAll().size();

        // Create the Conclusion with an existing ID
        conclusion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConclusionMockMvc.perform(post("/api/conclusions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(conclusion)))
            .andExpect(status().isBadRequest());

        // Validate the Conclusion in the database
        List<Conclusion> conclusionList = conclusionRepository.findAll();
        assertThat(conclusionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConclusions() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList
        restConclusionMockMvc.perform(get("/api/conclusions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conclusion.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].resultDescription").value(hasItem(DEFAULT_RESULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }
    
    @Test
    @Transactional
    public void getConclusion() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get the conclusion
        restConclusionMockMvc.perform(get("/api/conclusions/{id}", conclusion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conclusion.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.resultDescription").value(DEFAULT_RESULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }


    @Test
    @Transactional
    public void getConclusionsByIdFiltering() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        Long id = conclusion.getId();

        defaultConclusionShouldBeFound("id.equals=" + id);
        defaultConclusionShouldNotBeFound("id.notEquals=" + id);

        defaultConclusionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConclusionShouldNotBeFound("id.greaterThan=" + id);

        defaultConclusionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConclusionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllConclusionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date equals to DEFAULT_DATE
        defaultConclusionShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the conclusionList where date equals to UPDATED_DATE
        defaultConclusionShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConclusionsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date not equals to DEFAULT_DATE
        defaultConclusionShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the conclusionList where date not equals to UPDATED_DATE
        defaultConclusionShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConclusionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date in DEFAULT_DATE or UPDATED_DATE
        defaultConclusionShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the conclusionList where date equals to UPDATED_DATE
        defaultConclusionShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConclusionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date is not null
        defaultConclusionShouldBeFound("date.specified=true");

        // Get all the conclusionList where date is null
        defaultConclusionShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllConclusionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date is greater than or equal to DEFAULT_DATE
        defaultConclusionShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the conclusionList where date is greater than or equal to UPDATED_DATE
        defaultConclusionShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConclusionsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date is less than or equal to DEFAULT_DATE
        defaultConclusionShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the conclusionList where date is less than or equal to SMALLER_DATE
        defaultConclusionShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllConclusionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date is less than DEFAULT_DATE
        defaultConclusionShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the conclusionList where date is less than UPDATED_DATE
        defaultConclusionShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConclusionsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where date is greater than DEFAULT_DATE
        defaultConclusionShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the conclusionList where date is greater than SMALLER_DATE
        defaultConclusionShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllConclusionsByResultDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where resultDescription equals to DEFAULT_RESULT_DESCRIPTION
        defaultConclusionShouldBeFound("resultDescription.equals=" + DEFAULT_RESULT_DESCRIPTION);

        // Get all the conclusionList where resultDescription equals to UPDATED_RESULT_DESCRIPTION
        defaultConclusionShouldNotBeFound("resultDescription.equals=" + UPDATED_RESULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConclusionsByResultDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where resultDescription not equals to DEFAULT_RESULT_DESCRIPTION
        defaultConclusionShouldNotBeFound("resultDescription.notEquals=" + DEFAULT_RESULT_DESCRIPTION);

        // Get all the conclusionList where resultDescription not equals to UPDATED_RESULT_DESCRIPTION
        defaultConclusionShouldBeFound("resultDescription.notEquals=" + UPDATED_RESULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConclusionsByResultDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where resultDescription in DEFAULT_RESULT_DESCRIPTION or UPDATED_RESULT_DESCRIPTION
        defaultConclusionShouldBeFound("resultDescription.in=" + DEFAULT_RESULT_DESCRIPTION + "," + UPDATED_RESULT_DESCRIPTION);

        // Get all the conclusionList where resultDescription equals to UPDATED_RESULT_DESCRIPTION
        defaultConclusionShouldNotBeFound("resultDescription.in=" + UPDATED_RESULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConclusionsByResultDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where resultDescription is not null
        defaultConclusionShouldBeFound("resultDescription.specified=true");

        // Get all the conclusionList where resultDescription is null
        defaultConclusionShouldNotBeFound("resultDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllConclusionsByResultDescriptionContainsSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where resultDescription contains DEFAULT_RESULT_DESCRIPTION
        defaultConclusionShouldBeFound("resultDescription.contains=" + DEFAULT_RESULT_DESCRIPTION);

        // Get all the conclusionList where resultDescription contains UPDATED_RESULT_DESCRIPTION
        defaultConclusionShouldNotBeFound("resultDescription.contains=" + UPDATED_RESULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllConclusionsByResultDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where resultDescription does not contain DEFAULT_RESULT_DESCRIPTION
        defaultConclusionShouldNotBeFound("resultDescription.doesNotContain=" + DEFAULT_RESULT_DESCRIPTION);

        // Get all the conclusionList where resultDescription does not contain UPDATED_RESULT_DESCRIPTION
        defaultConclusionShouldBeFound("resultDescription.doesNotContain=" + UPDATED_RESULT_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllConclusionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where url equals to DEFAULT_URL
        defaultConclusionShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the conclusionList where url equals to UPDATED_URL
        defaultConclusionShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllConclusionsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where url not equals to DEFAULT_URL
        defaultConclusionShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the conclusionList where url not equals to UPDATED_URL
        defaultConclusionShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllConclusionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where url in DEFAULT_URL or UPDATED_URL
        defaultConclusionShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the conclusionList where url equals to UPDATED_URL
        defaultConclusionShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllConclusionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where url is not null
        defaultConclusionShouldBeFound("url.specified=true");

        // Get all the conclusionList where url is null
        defaultConclusionShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllConclusionsByUrlContainsSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where url contains DEFAULT_URL
        defaultConclusionShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the conclusionList where url contains UPDATED_URL
        defaultConclusionShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllConclusionsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusionList where url does not contain DEFAULT_URL
        defaultConclusionShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the conclusionList where url does not contain UPDATED_URL
        defaultConclusionShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllConclusionsBySignedByIsEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);
        Oncologist signedBy = OncologistResourceIT.createEntity(em);
        em.persist(signedBy);
        em.flush();
        conclusion.setSignedBy(signedBy);
        conclusionRepository.saveAndFlush(conclusion);
        Long signedById = signedBy.getId();

        // Get all the conclusionList where signedBy equals to signedById
        defaultConclusionShouldBeFound("signedById.equals=" + signedById);

        // Get all the conclusionList where signedBy equals to signedById + 1
        defaultConclusionShouldNotBeFound("signedById.equals=" + (signedById + 1));
    }


    @Test
    @Transactional
    public void getAllConclusionsByForPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);
        Patient forPatient = PatientResourceIT.createEntity(em);
        em.persist(forPatient);
        em.flush();
        conclusion.setForPatient(forPatient);
        conclusionRepository.saveAndFlush(conclusion);
        Long forPatientId = forPatient.getId();

        // Get all the conclusionList where forPatient equals to forPatientId
        defaultConclusionShouldBeFound("forPatientId.equals=" + forPatientId);

        // Get all the conclusionList where forPatient equals to forPatientId + 1
        defaultConclusionShouldNotBeFound("forPatientId.equals=" + (forPatientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConclusionShouldBeFound(String filter) throws Exception {
        restConclusionMockMvc.perform(get("/api/conclusions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conclusion.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].resultDescription").value(hasItem(DEFAULT_RESULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restConclusionMockMvc.perform(get("/api/conclusions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConclusionShouldNotBeFound(String filter) throws Exception {
        restConclusionMockMvc.perform(get("/api/conclusions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConclusionMockMvc.perform(get("/api/conclusions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingConclusion() throws Exception {
        // Get the conclusion
        restConclusionMockMvc.perform(get("/api/conclusions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConclusion() throws Exception {
        // Initialize the database
        conclusionService.save(conclusion);

        int databaseSizeBeforeUpdate = conclusionRepository.findAll().size();

        // Update the conclusion
        Conclusion updatedConclusion = conclusionRepository.findById(conclusion.getId()).get();
        // Disconnect from session so that the updates on updatedConclusion are not directly saved in db
        em.detach(updatedConclusion);
        updatedConclusion
            .date(UPDATED_DATE)
            .resultDescription(UPDATED_RESULT_DESCRIPTION)
            .url(UPDATED_URL);

        restConclusionMockMvc.perform(put("/api/conclusions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConclusion)))
            .andExpect(status().isOk());

        // Validate the Conclusion in the database
        List<Conclusion> conclusionList = conclusionRepository.findAll();
        assertThat(conclusionList).hasSize(databaseSizeBeforeUpdate);
        Conclusion testConclusion = conclusionList.get(conclusionList.size() - 1);
        assertThat(testConclusion.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConclusion.getResultDescription()).isEqualTo(UPDATED_RESULT_DESCRIPTION);
        assertThat(testConclusion.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingConclusion() throws Exception {
        int databaseSizeBeforeUpdate = conclusionRepository.findAll().size();

        // Create the Conclusion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConclusionMockMvc.perform(put("/api/conclusions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(conclusion)))
            .andExpect(status().isBadRequest());

        // Validate the Conclusion in the database
        List<Conclusion> conclusionList = conclusionRepository.findAll();
        assertThat(conclusionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConclusion() throws Exception {
        // Initialize the database
        conclusionService.save(conclusion);

        int databaseSizeBeforeDelete = conclusionRepository.findAll().size();

        // Delete the conclusion
        restConclusionMockMvc.perform(delete("/api/conclusions/{id}", conclusion.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conclusion> conclusionList = conclusionRepository.findAll();
        assertThat(conclusionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
