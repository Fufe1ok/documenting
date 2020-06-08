package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.AllscriptApp;
import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.domain.Conclusion;
import com.paneyko.allscript.domain.TestResult;
import com.paneyko.allscript.domain.Hospital;
import com.paneyko.allscript.domain.Patient;
import com.paneyko.allscript.repository.OncologistRepository;
import com.paneyko.allscript.service.OncologistService;
import com.paneyko.allscript.service.dto.OncologistCriteria;
import com.paneyko.allscript.service.OncologistQueryService;

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
 * Integration tests for the {@link OncologistResource} REST controller.
 */
@SpringBootTest(classes = AllscriptApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class OncologistResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "+0 9956330980";
    private static final String UPDATED_PHONE = "+218 3143771128";

    private static final Integer DEFAULT_ROOM_IN = 1;
    private static final Integer UPDATED_ROOM_IN = 2;
    private static final Integer SMALLER_ROOM_IN = 1 - 1;

    @Autowired
    private OncologistRepository oncologistRepository;

    @Autowired
    private OncologistService oncologistService;

    @Autowired
    private OncologistQueryService oncologistQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOncologistMockMvc;

    private Oncologist oncologist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oncologist createEntity(EntityManager em) {
        Oncologist oncologist = new Oncologist()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .roomIn(DEFAULT_ROOM_IN);
        return oncologist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oncologist createUpdatedEntity(EntityManager em) {
        Oncologist oncologist = new Oncologist()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .roomIn(UPDATED_ROOM_IN);
        return oncologist;
    }

    @BeforeEach
    public void initTest() {
        oncologist = createEntity(em);
    }

    @Test
    @Transactional
    public void createOncologist() throws Exception {
        int databaseSizeBeforeCreate = oncologistRepository.findAll().size();

        // Create the Oncologist
        restOncologistMockMvc.perform(post("/api/oncologists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oncologist)))
            .andExpect(status().isCreated());

        // Validate the Oncologist in the database
        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeCreate + 1);
        Oncologist testOncologist = oncologistList.get(oncologistList.size() - 1);
        assertThat(testOncologist.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOncologist.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testOncologist.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOncologist.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testOncologist.getRoomIn()).isEqualTo(DEFAULT_ROOM_IN);
    }

    @Test
    @Transactional
    public void createOncologistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = oncologistRepository.findAll().size();

        // Create the Oncologist with an existing ID
        oncologist.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOncologistMockMvc.perform(post("/api/oncologists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oncologist)))
            .andExpect(status().isBadRequest());

        // Validate the Oncologist in the database
        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = oncologistRepository.findAll().size();
        // set the field null
        oncologist.setName(null);

        // Create the Oncologist, which fails.

        restOncologistMockMvc.perform(post("/api/oncologists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oncologist)))
            .andExpect(status().isBadRequest());

        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = oncologistRepository.findAll().size();
        // set the field null
        oncologist.setSurname(null);

        // Create the Oncologist, which fails.

        restOncologistMockMvc.perform(post("/api/oncologists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oncologist)))
            .andExpect(status().isBadRequest());

        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = oncologistRepository.findAll().size();
        // set the field null
        oncologist.setEmail(null);

        // Create the Oncologist, which fails.

        restOncologistMockMvc.perform(post("/api/oncologists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oncologist)))
            .andExpect(status().isBadRequest());

        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOncologists() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList
        restOncologistMockMvc.perform(get("/api/oncologists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oncologist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].roomIn").value(hasItem(DEFAULT_ROOM_IN)));
    }
    
    @Test
    @Transactional
    public void getOncologist() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get the oncologist
        restOncologistMockMvc.perform(get("/api/oncologists/{id}", oncologist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oncologist.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.roomIn").value(DEFAULT_ROOM_IN));
    }


    @Test
    @Transactional
    public void getOncologistsByIdFiltering() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        Long id = oncologist.getId();

        defaultOncologistShouldBeFound("id.equals=" + id);
        defaultOncologistShouldNotBeFound("id.notEquals=" + id);

        defaultOncologistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOncologistShouldNotBeFound("id.greaterThan=" + id);

        defaultOncologistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOncologistShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOncologistsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where name equals to DEFAULT_NAME
        defaultOncologistShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the oncologistList where name equals to UPDATED_NAME
        defaultOncologistShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where name not equals to DEFAULT_NAME
        defaultOncologistShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the oncologistList where name not equals to UPDATED_NAME
        defaultOncologistShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOncologistShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the oncologistList where name equals to UPDATED_NAME
        defaultOncologistShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where name is not null
        defaultOncologistShouldBeFound("name.specified=true");

        // Get all the oncologistList where name is null
        defaultOncologistShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOncologistsByNameContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where name contains DEFAULT_NAME
        defaultOncologistShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the oncologistList where name contains UPDATED_NAME
        defaultOncologistShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where name does not contain DEFAULT_NAME
        defaultOncologistShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the oncologistList where name does not contain UPDATED_NAME
        defaultOncologistShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOncologistsBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where surname equals to DEFAULT_SURNAME
        defaultOncologistShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the oncologistList where surname equals to UPDATED_SURNAME
        defaultOncologistShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsBySurnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where surname not equals to DEFAULT_SURNAME
        defaultOncologistShouldNotBeFound("surname.notEquals=" + DEFAULT_SURNAME);

        // Get all the oncologistList where surname not equals to UPDATED_SURNAME
        defaultOncologistShouldBeFound("surname.notEquals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultOncologistShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the oncologistList where surname equals to UPDATED_SURNAME
        defaultOncologistShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where surname is not null
        defaultOncologistShouldBeFound("surname.specified=true");

        // Get all the oncologistList where surname is null
        defaultOncologistShouldNotBeFound("surname.specified=false");
    }
                @Test
    @Transactional
    public void getAllOncologistsBySurnameContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where surname contains DEFAULT_SURNAME
        defaultOncologistShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the oncologistList where surname contains UPDATED_SURNAME
        defaultOncologistShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllOncologistsBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where surname does not contain DEFAULT_SURNAME
        defaultOncologistShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the oncologistList where surname does not contain UPDATED_SURNAME
        defaultOncologistShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }


    @Test
    @Transactional
    public void getAllOncologistsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where email equals to DEFAULT_EMAIL
        defaultOncologistShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the oncologistList where email equals to UPDATED_EMAIL
        defaultOncologistShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOncologistsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where email not equals to DEFAULT_EMAIL
        defaultOncologistShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the oncologistList where email not equals to UPDATED_EMAIL
        defaultOncologistShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOncologistsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOncologistShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the oncologistList where email equals to UPDATED_EMAIL
        defaultOncologistShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOncologistsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where email is not null
        defaultOncologistShouldBeFound("email.specified=true");

        // Get all the oncologistList where email is null
        defaultOncologistShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllOncologistsByEmailContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where email contains DEFAULT_EMAIL
        defaultOncologistShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the oncologistList where email contains UPDATED_EMAIL
        defaultOncologistShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOncologistsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where email does not contain DEFAULT_EMAIL
        defaultOncologistShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the oncologistList where email does not contain UPDATED_EMAIL
        defaultOncologistShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllOncologistsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where phone equals to DEFAULT_PHONE
        defaultOncologistShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the oncologistList where phone equals to UPDATED_PHONE
        defaultOncologistShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOncologistsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where phone not equals to DEFAULT_PHONE
        defaultOncologistShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the oncologistList where phone not equals to UPDATED_PHONE
        defaultOncologistShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOncologistsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultOncologistShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the oncologistList where phone equals to UPDATED_PHONE
        defaultOncologistShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOncologistsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where phone is not null
        defaultOncologistShouldBeFound("phone.specified=true");

        // Get all the oncologistList where phone is null
        defaultOncologistShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllOncologistsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where phone contains DEFAULT_PHONE
        defaultOncologistShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the oncologistList where phone contains UPDATED_PHONE
        defaultOncologistShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOncologistsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where phone does not contain DEFAULT_PHONE
        defaultOncologistShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the oncologistList where phone does not contain UPDATED_PHONE
        defaultOncologistShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn equals to DEFAULT_ROOM_IN
        defaultOncologistShouldBeFound("roomIn.equals=" + DEFAULT_ROOM_IN);

        // Get all the oncologistList where roomIn equals to UPDATED_ROOM_IN
        defaultOncologistShouldNotBeFound("roomIn.equals=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn not equals to DEFAULT_ROOM_IN
        defaultOncologistShouldNotBeFound("roomIn.notEquals=" + DEFAULT_ROOM_IN);

        // Get all the oncologistList where roomIn not equals to UPDATED_ROOM_IN
        defaultOncologistShouldBeFound("roomIn.notEquals=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsInShouldWork() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn in DEFAULT_ROOM_IN or UPDATED_ROOM_IN
        defaultOncologistShouldBeFound("roomIn.in=" + DEFAULT_ROOM_IN + "," + UPDATED_ROOM_IN);

        // Get all the oncologistList where roomIn equals to UPDATED_ROOM_IN
        defaultOncologistShouldNotBeFound("roomIn.in=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsNullOrNotNull() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn is not null
        defaultOncologistShouldBeFound("roomIn.specified=true");

        // Get all the oncologistList where roomIn is null
        defaultOncologistShouldNotBeFound("roomIn.specified=false");
    }

    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn is greater than or equal to DEFAULT_ROOM_IN
        defaultOncologistShouldBeFound("roomIn.greaterThanOrEqual=" + DEFAULT_ROOM_IN);

        // Get all the oncologistList where roomIn is greater than or equal to UPDATED_ROOM_IN
        defaultOncologistShouldNotBeFound("roomIn.greaterThanOrEqual=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn is less than or equal to DEFAULT_ROOM_IN
        defaultOncologistShouldBeFound("roomIn.lessThanOrEqual=" + DEFAULT_ROOM_IN);

        // Get all the oncologistList where roomIn is less than or equal to SMALLER_ROOM_IN
        defaultOncologistShouldNotBeFound("roomIn.lessThanOrEqual=" + SMALLER_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsLessThanSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn is less than DEFAULT_ROOM_IN
        defaultOncologistShouldNotBeFound("roomIn.lessThan=" + DEFAULT_ROOM_IN);

        // Get all the oncologistList where roomIn is less than UPDATED_ROOM_IN
        defaultOncologistShouldBeFound("roomIn.lessThan=" + UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void getAllOncologistsByRoomInIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);

        // Get all the oncologistList where roomIn is greater than DEFAULT_ROOM_IN
        defaultOncologistShouldNotBeFound("roomIn.greaterThan=" + DEFAULT_ROOM_IN);

        // Get all the oncologistList where roomIn is greater than SMALLER_ROOM_IN
        defaultOncologistShouldBeFound("roomIn.greaterThan=" + SMALLER_ROOM_IN);
    }


    @Test
    @Transactional
    public void getAllOncologistsByConclusionsIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);
        Conclusion conclusions = ConclusionResourceIT.createEntity(em);
        em.persist(conclusions);
        em.flush();
        oncologist.addConclusions(conclusions);
        oncologistRepository.saveAndFlush(oncologist);
        Long conclusionsId = conclusions.getId();

        // Get all the oncologistList where conclusions equals to conclusionsId
        defaultOncologistShouldBeFound("conclusionsId.equals=" + conclusionsId);

        // Get all the oncologistList where conclusions equals to conclusionsId + 1
        defaultOncologistShouldNotBeFound("conclusionsId.equals=" + (conclusionsId + 1));
    }


    @Test
    @Transactional
    public void getAllOncologistsByTestsIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);
        TestResult tests = TestResultResourceIT.createEntity(em);
        em.persist(tests);
        em.flush();
        oncologist.addTests(tests);
        oncologistRepository.saveAndFlush(oncologist);
        Long testsId = tests.getId();

        // Get all the oncologistList where tests equals to testsId
        defaultOncologistShouldBeFound("testsId.equals=" + testsId);

        // Get all the oncologistList where tests equals to testsId + 1
        defaultOncologistShouldNotBeFound("testsId.equals=" + (testsId + 1));
    }


    @Test
    @Transactional
    public void getAllOncologistsByHospitalIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);
        Hospital hospital = HospitalResourceIT.createEntity(em);
        em.persist(hospital);
        em.flush();
        oncologist.setHospital(hospital);
        oncologistRepository.saveAndFlush(oncologist);
        Long hospitalId = hospital.getId();

        // Get all the oncologistList where hospital equals to hospitalId
        defaultOncologistShouldBeFound("hospitalId.equals=" + hospitalId);

        // Get all the oncologistList where hospital equals to hospitalId + 1
        defaultOncologistShouldNotBeFound("hospitalId.equals=" + (hospitalId + 1));
    }


    @Test
    @Transactional
    public void getAllOncologistsByPatientsIsEqualToSomething() throws Exception {
        // Initialize the database
        oncologistRepository.saveAndFlush(oncologist);
        Patient patients = PatientResourceIT.createEntity(em);
        em.persist(patients);
        em.flush();
        oncologist.addPatients(patients);
        oncologistRepository.saveAndFlush(oncologist);
        Long patientsId = patients.getId();

        // Get all the oncologistList where patients equals to patientsId
        defaultOncologistShouldBeFound("patientsId.equals=" + patientsId);

        // Get all the oncologistList where patients equals to patientsId + 1
        defaultOncologistShouldNotBeFound("patientsId.equals=" + (patientsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOncologistShouldBeFound(String filter) throws Exception {
        restOncologistMockMvc.perform(get("/api/oncologists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oncologist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].roomIn").value(hasItem(DEFAULT_ROOM_IN)));

        // Check, that the count call also returns 1
        restOncologistMockMvc.perform(get("/api/oncologists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOncologistShouldNotBeFound(String filter) throws Exception {
        restOncologistMockMvc.perform(get("/api/oncologists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOncologistMockMvc.perform(get("/api/oncologists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOncologist() throws Exception {
        // Get the oncologist
        restOncologistMockMvc.perform(get("/api/oncologists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOncologist() throws Exception {
        // Initialize the database
        oncologistService.save(oncologist);

        int databaseSizeBeforeUpdate = oncologistRepository.findAll().size();

        // Update the oncologist
        Oncologist updatedOncologist = oncologistRepository.findById(oncologist.getId()).get();
        // Disconnect from session so that the updates on updatedOncologist are not directly saved in db
        em.detach(updatedOncologist);
        updatedOncologist
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .roomIn(UPDATED_ROOM_IN);

        restOncologistMockMvc.perform(put("/api/oncologists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOncologist)))
            .andExpect(status().isOk());

        // Validate the Oncologist in the database
        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeUpdate);
        Oncologist testOncologist = oncologistList.get(oncologistList.size() - 1);
        assertThat(testOncologist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOncologist.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testOncologist.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOncologist.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOncologist.getRoomIn()).isEqualTo(UPDATED_ROOM_IN);
    }

    @Test
    @Transactional
    public void updateNonExistingOncologist() throws Exception {
        int databaseSizeBeforeUpdate = oncologistRepository.findAll().size();

        // Create the Oncologist

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOncologistMockMvc.perform(put("/api/oncologists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oncologist)))
            .andExpect(status().isBadRequest());

        // Validate the Oncologist in the database
        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOncologist() throws Exception {
        // Initialize the database
        oncologistService.save(oncologist);

        int databaseSizeBeforeDelete = oncologistRepository.findAll().size();

        // Delete the oncologist
        restOncologistMockMvc.perform(delete("/api/oncologists/{id}", oncologist.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Oncologist> oncologistList = oncologistRepository.findAll();
        assertThat(oncologistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
