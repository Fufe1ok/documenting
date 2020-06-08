package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.AllscriptApp;
import com.paneyko.allscript.domain.TestResult;
import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.domain.Patient;
import com.paneyko.allscript.repository.TestResultRepository;
import com.paneyko.allscript.service.TestResultService;
import com.paneyko.allscript.service.dto.TestResultCriteria;
import com.paneyko.allscript.service.TestResultQueryService;

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

import com.paneyko.allscript.domain.enumeration.TestType;
/**
 * Integration tests for the {@link TestResultResource} REST controller.
 */
@SpringBootTest(classes = AllscriptApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TestResultResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TestType DEFAULT_TYPE = TestType.GENERAL_BLOOD;
    private static final TestType UPDATED_TYPE = TestType.GISTOLOGY;

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private TestResultQueryService testResultQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestResultMockMvc;

    private TestResult testResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestResult createEntity(EntityManager em) {
        TestResult testResult = new TestResult()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .dateTime(DEFAULT_DATE_TIME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL);
        return testResult;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestResult createUpdatedEntity(EntityManager em) {
        TestResult testResult = new TestResult()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .dateTime(UPDATED_DATE_TIME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL);
        return testResult;
    }

    @BeforeEach
    public void initTest() {
        testResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestResult() throws Exception {
        int databaseSizeBeforeCreate = testResultRepository.findAll().size();

        // Create the TestResult
        restTestResultMockMvc.perform(post("/api/test-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(testResult)))
            .andExpect(status().isCreated());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeCreate + 1);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
        assertThat(testTestResult.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestResult.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTestResult.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testTestResult.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestResult.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createTestResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testResultRepository.findAll().size();

        // Create the TestResult with an existing ID
        testResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestResultMockMvc.perform(post("/api/test-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(testResult)))
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTestResults() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList
        restTestResultMockMvc.perform(get("/api/test-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }
    
    @Test
    @Transactional
    public void getTestResult() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get the testResult
        restTestResultMockMvc.perform(get("/api/test-results/{id}", testResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testResult.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }


    @Test
    @Transactional
    public void getTestResultsByIdFiltering() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        Long id = testResult.getId();

        defaultTestResultShouldBeFound("id.equals=" + id);
        defaultTestResultShouldNotBeFound("id.notEquals=" + id);

        defaultTestResultShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestResultShouldNotBeFound("id.greaterThan=" + id);

        defaultTestResultShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestResultShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTestResultsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where name equals to DEFAULT_NAME
        defaultTestResultShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testResultList where name equals to UPDATED_NAME
        defaultTestResultShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where name not equals to DEFAULT_NAME
        defaultTestResultShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the testResultList where name not equals to UPDATED_NAME
        defaultTestResultShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestResultShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testResultList where name equals to UPDATED_NAME
        defaultTestResultShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where name is not null
        defaultTestResultShouldBeFound("name.specified=true");

        // Get all the testResultList where name is null
        defaultTestResultShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllTestResultsByNameContainsSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where name contains DEFAULT_NAME
        defaultTestResultShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testResultList where name contains UPDATED_NAME
        defaultTestResultShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where name does not contain DEFAULT_NAME
        defaultTestResultShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testResultList where name does not contain UPDATED_NAME
        defaultTestResultShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllTestResultsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where type equals to DEFAULT_TYPE
        defaultTestResultShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the testResultList where type equals to UPDATED_TYPE
        defaultTestResultShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTestResultsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where type not equals to DEFAULT_TYPE
        defaultTestResultShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the testResultList where type not equals to UPDATED_TYPE
        defaultTestResultShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTestResultsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTestResultShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the testResultList where type equals to UPDATED_TYPE
        defaultTestResultShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTestResultsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where type is not null
        defaultTestResultShouldBeFound("type.specified=true");

        // Get all the testResultList where type is null
        defaultTestResultShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime equals to DEFAULT_DATE_TIME
        defaultTestResultShouldBeFound("dateTime.equals=" + DEFAULT_DATE_TIME);

        // Get all the testResultList where dateTime equals to UPDATED_DATE_TIME
        defaultTestResultShouldNotBeFound("dateTime.equals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime not equals to DEFAULT_DATE_TIME
        defaultTestResultShouldNotBeFound("dateTime.notEquals=" + DEFAULT_DATE_TIME);

        // Get all the testResultList where dateTime not equals to UPDATED_DATE_TIME
        defaultTestResultShouldBeFound("dateTime.notEquals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime in DEFAULT_DATE_TIME or UPDATED_DATE_TIME
        defaultTestResultShouldBeFound("dateTime.in=" + DEFAULT_DATE_TIME + "," + UPDATED_DATE_TIME);

        // Get all the testResultList where dateTime equals to UPDATED_DATE_TIME
        defaultTestResultShouldNotBeFound("dateTime.in=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime is not null
        defaultTestResultShouldBeFound("dateTime.specified=true");

        // Get all the testResultList where dateTime is null
        defaultTestResultShouldNotBeFound("dateTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime is greater than or equal to DEFAULT_DATE_TIME
        defaultTestResultShouldBeFound("dateTime.greaterThanOrEqual=" + DEFAULT_DATE_TIME);

        // Get all the testResultList where dateTime is greater than or equal to UPDATED_DATE_TIME
        defaultTestResultShouldNotBeFound("dateTime.greaterThanOrEqual=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime is less than or equal to DEFAULT_DATE_TIME
        defaultTestResultShouldBeFound("dateTime.lessThanOrEqual=" + DEFAULT_DATE_TIME);

        // Get all the testResultList where dateTime is less than or equal to SMALLER_DATE_TIME
        defaultTestResultShouldNotBeFound("dateTime.lessThanOrEqual=" + SMALLER_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime is less than DEFAULT_DATE_TIME
        defaultTestResultShouldNotBeFound("dateTime.lessThan=" + DEFAULT_DATE_TIME);

        // Get all the testResultList where dateTime is less than UPDATED_DATE_TIME
        defaultTestResultShouldBeFound("dateTime.lessThan=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where dateTime is greater than DEFAULT_DATE_TIME
        defaultTestResultShouldNotBeFound("dateTime.greaterThan=" + DEFAULT_DATE_TIME);

        // Get all the testResultList where dateTime is greater than SMALLER_DATE_TIME
        defaultTestResultShouldBeFound("dateTime.greaterThan=" + SMALLER_DATE_TIME);
    }


    @Test
    @Transactional
    public void getAllTestResultsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where description equals to DEFAULT_DESCRIPTION
        defaultTestResultShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the testResultList where description equals to UPDATED_DESCRIPTION
        defaultTestResultShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where description not equals to DEFAULT_DESCRIPTION
        defaultTestResultShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the testResultList where description not equals to UPDATED_DESCRIPTION
        defaultTestResultShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTestResultShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the testResultList where description equals to UPDATED_DESCRIPTION
        defaultTestResultShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where description is not null
        defaultTestResultShouldBeFound("description.specified=true");

        // Get all the testResultList where description is null
        defaultTestResultShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllTestResultsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where description contains DEFAULT_DESCRIPTION
        defaultTestResultShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the testResultList where description contains UPDATED_DESCRIPTION
        defaultTestResultShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTestResultsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where description does not contain DEFAULT_DESCRIPTION
        defaultTestResultShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the testResultList where description does not contain UPDATED_DESCRIPTION
        defaultTestResultShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllTestResultsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where url equals to DEFAULT_URL
        defaultTestResultShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the testResultList where url equals to UPDATED_URL
        defaultTestResultShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllTestResultsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where url not equals to DEFAULT_URL
        defaultTestResultShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the testResultList where url not equals to UPDATED_URL
        defaultTestResultShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllTestResultsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where url in DEFAULT_URL or UPDATED_URL
        defaultTestResultShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the testResultList where url equals to UPDATED_URL
        defaultTestResultShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllTestResultsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where url is not null
        defaultTestResultShouldBeFound("url.specified=true");

        // Get all the testResultList where url is null
        defaultTestResultShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllTestResultsByUrlContainsSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where url contains DEFAULT_URL
        defaultTestResultShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the testResultList where url contains UPDATED_URL
        defaultTestResultShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllTestResultsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList where url does not contain DEFAULT_URL
        defaultTestResultShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the testResultList where url does not contain UPDATED_URL
        defaultTestResultShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllTestResultsByOncologistIsEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);
        Oncologist oncologist = OncologistResourceIT.createEntity(em);
        em.persist(oncologist);
        em.flush();
        testResult.setOncologist(oncologist);
        testResultRepository.saveAndFlush(testResult);
        Long oncologistId = oncologist.getId();

        // Get all the testResultList where oncologist equals to oncologistId
        defaultTestResultShouldBeFound("oncologistId.equals=" + oncologistId);

        // Get all the testResultList where oncologist equals to oncologistId + 1
        defaultTestResultShouldNotBeFound("oncologistId.equals=" + (oncologistId + 1));
    }


    @Test
    @Transactional
    public void getAllTestResultsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        testResult.setPatient(patient);
        testResultRepository.saveAndFlush(testResult);
        Long patientId = patient.getId();

        // Get all the testResultList where patient equals to patientId
        defaultTestResultShouldBeFound("patientId.equals=" + patientId);

        // Get all the testResultList where patient equals to patientId + 1
        defaultTestResultShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestResultShouldBeFound(String filter) throws Exception {
        restTestResultMockMvc.perform(get("/api/test-results?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restTestResultMockMvc.perform(get("/api/test-results/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestResultShouldNotBeFound(String filter) throws Exception {
        restTestResultMockMvc.perform(get("/api/test-results?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestResultMockMvc.perform(get("/api/test-results/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTestResult() throws Exception {
        // Get the testResult
        restTestResultMockMvc.perform(get("/api/test-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestResult() throws Exception {
        // Initialize the database
        testResultService.save(testResult);

        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Update the testResult
        TestResult updatedTestResult = testResultRepository.findById(testResult.getId()).get();
        // Disconnect from session so that the updates on updatedTestResult are not directly saved in db
        em.detach(updatedTestResult);
        updatedTestResult
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .dateTime(UPDATED_DATE_TIME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL);

        restTestResultMockMvc.perform(put("/api/test-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTestResult)))
            .andExpect(status().isOk());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
        assertThat(testTestResult.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestResult.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTestResult.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testTestResult.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestResult.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Create the TestResult

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestResultMockMvc.perform(put("/api/test-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(testResult)))
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTestResult() throws Exception {
        // Initialize the database
        testResultService.save(testResult);

        int databaseSizeBeforeDelete = testResultRepository.findAll().size();

        // Delete the testResult
        restTestResultMockMvc.perform(delete("/api/test-results/{id}", testResult.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
