package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.AllscriptApp;
import com.paneyko.allscript.domain.Patient;
import com.paneyko.allscript.domain.Conclusion;
import com.paneyko.allscript.domain.TestResult;
import com.paneyko.allscript.domain.Therapist;
import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.repository.PatientRepository;
import com.paneyko.allscript.service.PatientService;
import com.paneyko.allscript.service.dto.PatientCriteria;
import com.paneyko.allscript.service.PatientQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@SpringBootTest(classes = AllscriptApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PatientResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;
    private static final Double SMALLER_WEIGHT = 1D - 1D;

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;
    private static final Double SMALLER_HEIGHT = 1D - 1D;

    private static final String DEFAULT_PHONE = "1621631171";
    private static final String UPDATED_PHONE = "6162430173";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private PatientRepository patientRepository;

    @Mock
    private PatientRepository patientRepositoryMock;

    @Mock
    private PatientService patientServiceMock;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientQueryService patientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .age(DEFAULT_AGE)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS);
        return patient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .age(UPDATED_AGE)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatient.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatient.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatient.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testPatient.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPatient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPatient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setName(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setSurname(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setEmail(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPatientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(patientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientMockMvc.perform(get("/api/patients?eagerload=true"))
            .andExpect(status().isOk());

        verify(patientServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPatientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(patientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientMockMvc.perform(get("/api/patients?eagerload=true"))
            .andExpect(status().isOk());

        verify(patientServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }


    @Test
    @Transactional
    public void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPatientsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where name equals to DEFAULT_NAME
        defaultPatientShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the patientList where name equals to UPDATED_NAME
        defaultPatientShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where name not equals to DEFAULT_NAME
        defaultPatientShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the patientList where name not equals to UPDATED_NAME
        defaultPatientShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPatientShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the patientList where name equals to UPDATED_NAME
        defaultPatientShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where name is not null
        defaultPatientShouldBeFound("name.specified=true");

        // Get all the patientList where name is null
        defaultPatientShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where name contains DEFAULT_NAME
        defaultPatientShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the patientList where name contains UPDATED_NAME
        defaultPatientShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where name does not contain DEFAULT_NAME
        defaultPatientShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the patientList where name does not contain UPDATED_NAME
        defaultPatientShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPatientsBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname equals to DEFAULT_SURNAME
        defaultPatientShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the patientList where surname equals to UPDATED_SURNAME
        defaultPatientShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname not equals to DEFAULT_SURNAME
        defaultPatientShouldNotBeFound("surname.notEquals=" + DEFAULT_SURNAME);

        // Get all the patientList where surname not equals to UPDATED_SURNAME
        defaultPatientShouldBeFound("surname.notEquals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultPatientShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the patientList where surname equals to UPDATED_SURNAME
        defaultPatientShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname is not null
        defaultPatientShouldBeFound("surname.specified=true");

        // Get all the patientList where surname is null
        defaultPatientShouldNotBeFound("surname.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsBySurnameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname contains DEFAULT_SURNAME
        defaultPatientShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the patientList where surname contains UPDATED_SURNAME
        defaultPatientShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where surname does not contain DEFAULT_SURNAME
        defaultPatientShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the patientList where surname does not contain UPDATED_SURNAME
        defaultPatientShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }


    @Test
    @Transactional
    public void getAllPatientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email equals to DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email not equals to DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the patientList where email not equals to UPDATED_EMAIL
        defaultPatientShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPatientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email is not null
        defaultPatientShouldBeFound("email.specified=true");

        // Get all the patientList where email is null
        defaultPatientShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email contains DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the patientList where email contains UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email does not contain DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the patientList where email does not contain UPDATED_EMAIL
        defaultPatientShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllPatientsByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age equals to DEFAULT_AGE
        defaultPatientShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the patientList where age equals to UPDATED_AGE
        defaultPatientShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPatientsByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age not equals to DEFAULT_AGE
        defaultPatientShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the patientList where age not equals to UPDATED_AGE
        defaultPatientShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPatientsByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age in DEFAULT_AGE or UPDATED_AGE
        defaultPatientShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the patientList where age equals to UPDATED_AGE
        defaultPatientShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPatientsByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age is not null
        defaultPatientShouldBeFound("age.specified=true");

        // Get all the patientList where age is null
        defaultPatientShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age is greater than or equal to DEFAULT_AGE
        defaultPatientShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the patientList where age is greater than or equal to UPDATED_AGE
        defaultPatientShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPatientsByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age is less than or equal to DEFAULT_AGE
        defaultPatientShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the patientList where age is less than or equal to SMALLER_AGE
        defaultPatientShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    public void getAllPatientsByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age is less than DEFAULT_AGE
        defaultPatientShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the patientList where age is less than UPDATED_AGE
        defaultPatientShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllPatientsByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where age is greater than DEFAULT_AGE
        defaultPatientShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the patientList where age is greater than SMALLER_AGE
        defaultPatientShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }


    @Test
    @Transactional
    public void getAllPatientsByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight equals to DEFAULT_WEIGHT
        defaultPatientShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the patientList where weight equals to UPDATED_WEIGHT
        defaultPatientShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByWeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight not equals to DEFAULT_WEIGHT
        defaultPatientShouldNotBeFound("weight.notEquals=" + DEFAULT_WEIGHT);

        // Get all the patientList where weight not equals to UPDATED_WEIGHT
        defaultPatientShouldBeFound("weight.notEquals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultPatientShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the patientList where weight equals to UPDATED_WEIGHT
        defaultPatientShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight is not null
        defaultPatientShouldBeFound("weight.specified=true");

        // Get all the patientList where weight is null
        defaultPatientShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight is greater than or equal to DEFAULT_WEIGHT
        defaultPatientShouldBeFound("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the patientList where weight is greater than or equal to UPDATED_WEIGHT
        defaultPatientShouldNotBeFound("weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight is less than or equal to DEFAULT_WEIGHT
        defaultPatientShouldBeFound("weight.lessThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the patientList where weight is less than or equal to SMALLER_WEIGHT
        defaultPatientShouldNotBeFound("weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight is less than DEFAULT_WEIGHT
        defaultPatientShouldNotBeFound("weight.lessThan=" + DEFAULT_WEIGHT);

        // Get all the patientList where weight is less than UPDATED_WEIGHT
        defaultPatientShouldBeFound("weight.lessThan=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where weight is greater than DEFAULT_WEIGHT
        defaultPatientShouldNotBeFound("weight.greaterThan=" + DEFAULT_WEIGHT);

        // Get all the patientList where weight is greater than SMALLER_WEIGHT
        defaultPatientShouldBeFound("weight.greaterThan=" + SMALLER_WEIGHT);
    }


    @Test
    @Transactional
    public void getAllPatientsByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height equals to DEFAULT_HEIGHT
        defaultPatientShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the patientList where height equals to UPDATED_HEIGHT
        defaultPatientShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByHeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height not equals to DEFAULT_HEIGHT
        defaultPatientShouldNotBeFound("height.notEquals=" + DEFAULT_HEIGHT);

        // Get all the patientList where height not equals to UPDATED_HEIGHT
        defaultPatientShouldBeFound("height.notEquals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultPatientShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the patientList where height equals to UPDATED_HEIGHT
        defaultPatientShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height is not null
        defaultPatientShouldBeFound("height.specified=true");

        // Get all the patientList where height is null
        defaultPatientShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height is greater than or equal to DEFAULT_HEIGHT
        defaultPatientShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the patientList where height is greater than or equal to UPDATED_HEIGHT
        defaultPatientShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height is less than or equal to DEFAULT_HEIGHT
        defaultPatientShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the patientList where height is less than or equal to SMALLER_HEIGHT
        defaultPatientShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height is less than DEFAULT_HEIGHT
        defaultPatientShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the patientList where height is less than UPDATED_HEIGHT
        defaultPatientShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPatientsByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where height is greater than DEFAULT_HEIGHT
        defaultPatientShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the patientList where height is greater than SMALLER_HEIGHT
        defaultPatientShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }


    @Test
    @Transactional
    public void getAllPatientsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone equals to DEFAULT_PHONE
        defaultPatientShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the patientList where phone equals to UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone not equals to DEFAULT_PHONE
        defaultPatientShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the patientList where phone not equals to UPDATED_PHONE
        defaultPatientShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPatientShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the patientList where phone equals to UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone is not null
        defaultPatientShouldBeFound("phone.specified=true");

        // Get all the patientList where phone is null
        defaultPatientShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone contains DEFAULT_PHONE
        defaultPatientShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the patientList where phone contains UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone does not contain DEFAULT_PHONE
        defaultPatientShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the patientList where phone does not contain UPDATED_PHONE
        defaultPatientShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllPatientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address equals to DEFAULT_ADDRESS
        defaultPatientShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the patientList where address equals to UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address not equals to DEFAULT_ADDRESS
        defaultPatientShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the patientList where address not equals to UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the patientList where address equals to UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address is not null
        defaultPatientShouldBeFound("address.specified=true");

        // Get all the patientList where address is null
        defaultPatientShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByAddressContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address contains DEFAULT_ADDRESS
        defaultPatientShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the patientList where address contains UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address does not contain DEFAULT_ADDRESS
        defaultPatientShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the patientList where address does not contain UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllPatientsByConclusionsIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Conclusion conclusions = ConclusionResourceIT.createEntity(em);
        em.persist(conclusions);
        em.flush();
        patient.addConclusions(conclusions);
        patientRepository.saveAndFlush(patient);
        Long conclusionsId = conclusions.getId();

        // Get all the patientList where conclusions equals to conclusionsId
        defaultPatientShouldBeFound("conclusionsId.equals=" + conclusionsId);

        // Get all the patientList where conclusions equals to conclusionsId + 1
        defaultPatientShouldNotBeFound("conclusionsId.equals=" + (conclusionsId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByTestsIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        TestResult tests = TestResultResourceIT.createEntity(em);
        em.persist(tests);
        em.flush();
        patient.addTests(tests);
        patientRepository.saveAndFlush(patient);
        Long testsId = tests.getId();

        // Get all the patientList where tests equals to testsId
        defaultPatientShouldBeFound("testsId.equals=" + testsId);

        // Get all the patientList where tests equals to testsId + 1
        defaultPatientShouldNotBeFound("testsId.equals=" + (testsId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByTherapistsIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Therapist therapists = TherapistResourceIT.createEntity(em);
        em.persist(therapists);
        em.flush();
        patient.addTherapists(therapists);
        patientRepository.saveAndFlush(patient);
        Long therapistsId = therapists.getId();

        // Get all the patientList where therapists equals to therapistsId
        defaultPatientShouldBeFound("therapistsId.equals=" + therapistsId);

        // Get all the patientList where therapists equals to therapistsId + 1
        defaultPatientShouldNotBeFound("therapistsId.equals=" + (therapistsId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByOncologistsIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Oncologist oncologists = OncologistResourceIT.createEntity(em);
        em.persist(oncologists);
        em.flush();
        patient.addOncologists(oncologists);
        patientRepository.saveAndFlush(patient);
        Long oncologistsId = oncologists.getId();

        // Get all the patientList where oncologists equals to oncologistsId
        defaultPatientShouldBeFound("oncologistsId.equals=" + oncologistsId);

        // Get all the patientList where oncologists equals to oncologistsId + 1
        defaultPatientShouldNotBeFound("oncologistsId.equals=" + (oncologistsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .age(UPDATED_AGE)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatient.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatient.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPatient.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPatient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPatient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Create the Patient

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
