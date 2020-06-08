package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.AllscriptApp;
import com.paneyko.allscript.domain.Therapist;
import com.paneyko.allscript.domain.Hospital;
import com.paneyko.allscript.domain.Patient;
import com.paneyko.allscript.repository.TherapistRepository;
import com.paneyko.allscript.service.TherapistService;
import com.paneyko.allscript.service.dto.TherapistCriteria;
import com.paneyko.allscript.service.TherapistQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TherapistResource} REST controller.
 */
@SpringBootTest(classes = AllscriptApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TherapistResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "+3-9559993256";
    private static final String UPDATED_PHONE = "5893959150";

    private static final Integer DEFAULT_ROOM_IN = 1;
    private static final Integer UPDATED_ROOM_IN = 2;
    private static final Integer SMALLER_ROOM_IN = 1 - 1;

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private TherapistService therapistService;

    @Autowired
    private TherapistQueryService therapistQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTherapistMockMvc;

    private Therapist therapist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Therapist createEntity(EntityManager em) {
        Therapist therapist = new Therapist()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .roomIn(DEFAULT_ROOM_IN);
        return therapist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Therapist createUpdatedEntity(EntityManager em) {
        Therapist therapist = new Therapist()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .roomIn(UPDATED_ROOM_IN);
        return therapist;
    }

    @BeforeEach
    public void initTest() {
        therapist = createEntity(em);
    }

    @Test
    @Transactional
    public void createTherapist() throws Exception {
        int databaseSizeBeforeCreate = therapistRepository.findAll().size();

        // Create the Therapist
        restTherapistMockMvc.perform(post("/api/therapists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isCreated());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeCreate + 1);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTherapist.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testTherapist.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTherapist.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTherapist.getRoomIn()).isEqualTo(DEFAULT_ROOM_IN);
    }

    @Test
    @Transactional
    public void createTherapistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = therapistRepository.findAll().size();

        // Create the Therapist with an existing ID
        therapist.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTherapistMockMvc.perform(post("/api/therapists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isBadRequest());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = therapistRepository.findAll().size();
        // set the field null
        therapist.setName(null);

        // Create the Therapist, which fails.

        restTherapistMockMvc.perform(post("/api/therapists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isBadRequest());

        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = therapistRepository.findAll().size();
        // set the field null
        therapist.setSurname(null);

        // Create the Therapist, which fails.

        restTherapistMockMvc.perform(post("/api/therapists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isBadRequest());

        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = therapistRepository.findAll().size();
        // set the field null
        therapist.setEmail(null);

        // Create the Therapist, which fails.

        restTherapistMockMvc.perform(post("/api/therapists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isBadRequest());

        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTherapists() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList
        restTherapistMockMvc.perform(get("/api/therapists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].roomIn").value(hasItem(DEFAULT_ROOM_IN)));
    }
    
    @Test
    @Transactional
    public void getTherapist() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get the therapist
        restTherapistMockMvc.perform(get("/api/therapists/{id}", therapist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(therapist.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.roomIn").value(DEFAULT_ROOM_IN));
    }


    @Test
    @Transactional
    public void getTherapistsByIdFiltering() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        Long id = therapist.getId();

        defaultTherapistShouldBeFound("id.equals=" + id);
        defaultTherapistShouldNotBeFound("id.notEquals=" + id);

        defaultTherapistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTherapistShouldNotBeFound("id.greaterThan=" + id);

        defaultTherapistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTherapistShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTherapistsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where name equals to DEFAULT_NAME
        defaultTherapistShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the therapistList where name equals to UPDATED_NAME
        defaultTherapistShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where name not equals to DEFAULT_NAME
        defaultTherapistShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the therapistList where name not equals to UPDATED_NAME
        defaultTherapistShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTherapistShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the therapistList where name equals to UPDATED_NAME
        defaultTherapistShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where name is not null
        defaultTherapistShouldBeFound("name.specified=true");

        // Get all the therapistList where name is null
        defaultTherapistShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllTherapistsByNameContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where name contains DEFAULT_NAME
        defaultTherapistShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the therapistList where name contains UPDATED_NAME
        defaultTherapistShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where name does not contain DEFAULT_NAME
        defaultTherapistShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the therapistList where name does not contain UPDATED_NAME
        defaultTherapistShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllTherapistsBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where surname equals to DEFAULT_SURNAME
        defaultTherapistShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the therapistList where surname equals to UPDATED_SURNAME
        defaultTherapistShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsBySurnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where surname not equals to DEFAULT_SURNAME
        defaultTherapistShouldNotBeFound("surname.notEquals=" + DEFAULT_SURNAME);

        // Get all the therapistList where surname not equals to UPDATED_SURNAME
        defaultTherapistShouldBeFound("surname.notEquals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultTherapistShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the therapistList where surname equals to UPDATED_SURNAME
        defaultTherapistShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where surname is not null
        defaultTherapistShouldBeFound("surname.specified=true");

        // Get all the therapistList where surname is null
        defaultTherapistShouldNotBeFound("surname.specified=false");
    }
                @Test
    @Transactional
    public void getAllTherapistsBySurnameContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where surname contains DEFAULT_SURNAME
        defaultTherapistShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the therapistList where surname contains UPDATED_SURNAME
        defaultTherapistShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllTherapistsBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where surname does not contain DEFAULT_SURNAME
        defaultTherapistShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the therapistList where surname does not contain UPDATED_SURNAME
        defaultTherapistShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }


    @Test
    @Transactional
    public void getAllTherapistsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where email equals to DEFAULT_EMAIL
        defaultTherapistShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the therapistList where email equals to UPDATED_EMAIL
        defaultTherapistShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTherapistsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where email not equals to DEFAULT_EMAIL
        defaultTherapistShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the therapistList where email not equals to UPDATED_EMAIL
        defaultTherapistShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTherapistsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTherapistShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the therapistList where email equals to UPDATED_EMAIL
        defaultTherapistShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTherapistsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where email is not null
        defaultTherapistShouldBeFound("email.specified=true");

        // Get all the therapistList where email is null
        defaultTherapistShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllTherapistsByEmailContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where email contains DEFAULT_EMAIL
        defaultTherapistShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the therapistList where email contains UPDATED_EMAIL
        defaultTherapistShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTherapistsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where email does not contain DEFAULT_EMAIL
        defaultTherapistShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the therapistList where email does not contain UPDATED_EMAIL
        defaultTherapistShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllTherapistsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where phone equals to DEFAULT_PHONE
        defaultTherapistShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the therapistList where phone equals to UPDATED_PHONE
        defaultTherapistShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTherapistsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where phone not equals to DEFAULT_PHONE
        defaultTherapistShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the therapistList where phone not equals to UPDATED_PHONE
        defaultTherapistShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTherapistsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultTherapistShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the therapistList where phone equals to UPDATED_PHONE
        defaultTherapistShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTherapistsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where phone is not null
        defaultTherapistShouldBeFound("phone.specified=true");

        // Get all the therapistList where phone is null
        defaultTherapistShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllTherapistsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where phone contains DEFAULT_PHONE
        defaultTherapistShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the therapistList where phone contains UPDATED_PHONE
        defaultTherapistShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTherapistsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where phone does not contain DEFAULT_PHONE
        defaultTherapistShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the therapistList where phone does not contain UPDATED_PHONE
        defaultTherapistShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn equals to DEFAULT_ROOM_IN
        defaultTherapistShouldBeFound("roomIn.equals=" + DEFAULT_ROOM_IN);

        // Get all the therapistList where roomIn equals to UPDATED_ROOM_IN
        defaultTherapistShouldNotBeFound("roomIn.equals=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsNotEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn not equals to DEFAULT_ROOM_IN
        defaultTherapistShouldNotBeFound("roomIn.notEquals=" + DEFAULT_ROOM_IN);

        // Get all the therapistList where roomIn not equals to UPDATED_ROOM_IN
        defaultTherapistShouldBeFound("roomIn.notEquals=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsInShouldWork() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn in DEFAULT_ROOM_IN or UPDATED_ROOM_IN
        defaultTherapistShouldBeFound("roomIn.in=" + DEFAULT_ROOM_IN + "," + UPDATED_ROOM_IN);

        // Get all the therapistList where roomIn equals to UPDATED_ROOM_IN
        defaultTherapistShouldNotBeFound("roomIn.in=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsNullOrNotNull() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn is not null
        defaultTherapistShouldBeFound("roomIn.specified=true");

        // Get all the therapistList where roomIn is null
        defaultTherapistShouldNotBeFound("roomIn.specified=false");
    }

    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn is greater than or equal to DEFAULT_ROOM_IN
        defaultTherapistShouldBeFound("roomIn.greaterThanOrEqual=" + DEFAULT_ROOM_IN);

        // Get all the therapistList where roomIn is greater than or equal to UPDATED_ROOM_IN
        defaultTherapistShouldNotBeFound("roomIn.greaterThanOrEqual=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn is less than or equal to DEFAULT_ROOM_IN
        defaultTherapistShouldBeFound("roomIn.lessThanOrEqual=" + DEFAULT_ROOM_IN);

        // Get all the therapistList where roomIn is less than or equal to SMALLER_ROOM_IN
        defaultTherapistShouldNotBeFound("roomIn.lessThanOrEqual=" + SMALLER_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsLessThanSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn is less than DEFAULT_ROOM_IN
        defaultTherapistShouldNotBeFound("roomIn.lessThan=" + DEFAULT_ROOM_IN);

        // Get all the therapistList where roomIn is less than UPDATED_ROOM_IN
        defaultTherapistShouldBeFound("roomIn.lessThan=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllTherapistsByRoomInIsGreaterThanSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList where roomIn is greater than DEFAULT_ROOM_IN
        defaultTherapistShouldNotBeFound("roomIn.greaterThan=" + DEFAULT_ROOM_IN);

        // Get all the therapistList where roomIn is greater than SMALLER_ROOM_IN
        defaultTherapistShouldBeFound("roomIn.greaterThan=" + SMALLER_ROOM_IN);
    }


    @Test
    @Transactional
    public void getAllTherapistsByHospitalIsEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);
        Hospital hospital = HospitalResourceIT.createEntity(em);
        em.persist(hospital);
        em.flush();
        therapist.setHospital(hospital);
        therapistRepository.saveAndFlush(therapist);
        Long hospitalId = hospital.getId();

        // Get all the therapistList where hospital equals to hospitalId
        defaultTherapistShouldBeFound("hospitalId.equals=" + hospitalId);

        // Get all the therapistList where hospital equals to hospitalId + 1
        defaultTherapistShouldNotBeFound("hospitalId.equals=" + (hospitalId + 1));
    }


    @Test
    @Transactional
    public void getAllTherapistsByPatientsIsEqualToSomething() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);
        Patient patients = PatientResourceIT.createEntity(em);
        em.persist(patients);
        em.flush();
        therapist.addPatients(patients);
        therapistRepository.saveAndFlush(therapist);
        Long patientsId = patients.getId();

        // Get all the therapistList where patients equals to patientsId
        defaultTherapistShouldBeFound("patientsId.equals=" + patientsId);

        // Get all the therapistList where patients equals to patientsId + 1
        defaultTherapistShouldNotBeFound("patientsId.equals=" + (patientsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTherapistShouldBeFound(String filter) throws Exception {
        restTherapistMockMvc.perform(get("/api/therapists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].roomIn").value(hasItem(DEFAULT_ROOM_IN)));

        // Check, that the count call also returns 1
        restTherapistMockMvc.perform(get("/api/therapists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTherapistShouldNotBeFound(String filter) throws Exception {
        restTherapistMockMvc.perform(get("/api/therapists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTherapistMockMvc.perform(get("/api/therapists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTherapist() throws Exception {
        // Get the therapist
        restTherapistMockMvc.perform(get("/api/therapists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTherapist() throws Exception {
        // Initialize the database
        therapistService.save(therapist);

        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();

        // Update the therapist
        Therapist updatedTherapist = therapistRepository.findById(therapist.getId()).get();
        // Disconnect from session so that the updates on updatedTherapist are not directly saved in db
        em.detach(updatedTherapist);
        updatedTherapist
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .roomIn(UPDATED_ROOM_IN);

        restTherapistMockMvc.perform(put("/api/therapists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTherapist)))
            .andExpect(status().isOk());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTherapist.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testTherapist.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTherapist.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTherapist.getRoomIn()).isEqualTo(UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void updateNonExistingTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();

        // Create the Therapist

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTherapistMockMvc.perform(put("/api/therapists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isBadRequest());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTherapist() throws Exception {
        // Initialize the database
        therapistService.save(therapist);

        int databaseSizeBeforeDelete = therapistRepository.findAll().size();

        // Delete the therapist
        restTherapistMockMvc.perform(delete("/api/therapists/{id}", therapist.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
