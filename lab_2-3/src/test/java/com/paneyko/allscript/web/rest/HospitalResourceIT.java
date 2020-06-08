package com.paneyko.allscript.web.rest;

import com.paneyko.allscript.AllscriptApp;
import com.paneyko.allscript.domain.Hospital;
import com.paneyko.allscript.domain.Oncologist;
import com.paneyko.allscript.domain.Therapist;
import com.paneyko.allscript.repository.HospitalRepository;
import com.paneyko.allscript.service.HospitalService;
import com.paneyko.allscript.service.dto.HospitalCriteria;
import com.paneyko.allscript.service.HospitalQueryService;

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
 * Integration tests for the {@link HospitalResource} REST controller.
 */
@SpringBootTest(classes = AllscriptApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class HospitalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "8280471250";
    private static final String UPDATED_PHONE = "+67-1287846741";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAID_FOR = false;
    private static final Boolean UPDATED_PAID_FOR = true;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalQueryService hospitalQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHospitalMockMvc;

    private Hospital hospital;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .paidFor(DEFAULT_PAID_FOR);
        return hospital;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createUpdatedEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .paidFor(UPDATED_PAID_FOR);
        return hospital;
    }

    @BeforeEach
    public void initTest() {
        hospital = createEntity(em);
    }

    @Test
    @Transactional
    public void createHospital() throws Exception {
        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();

        // Create the Hospital
        restHospitalMockMvc.perform(post("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isCreated());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate + 1);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testHospital.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHospital.isPaidFor()).isEqualTo(DEFAULT_PAID_FOR);
    }

    @Test
    @Transactional
    public void createHospitalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();

        // Create the Hospital with an existing ID
        hospital.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalMockMvc.perform(post("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalRepository.findAll().size();
        // set the field null
        hospital.setName(null);

        // Create the Hospital, which fails.

        restHospitalMockMvc.perform(post("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isBadRequest());

        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHospitals() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList
        restHospitalMockMvc.perform(get("/api/hospitals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].paidFor").value(hasItem(DEFAULT_PAID_FOR.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get the hospital
        restHospitalMockMvc.perform(get("/api/hospitals/{id}", hospital.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hospital.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.paidFor").value(DEFAULT_PAID_FOR.booleanValue()));
    }


    @Test
    @Transactional
    public void getHospitalsByIdFiltering() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        Long id = hospital.getId();

        defaultHospitalShouldBeFound("id.equals=" + id);
        defaultHospitalShouldNotBeFound("id.notEquals=" + id);

        defaultHospitalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.greaterThan=" + id);

        defaultHospitalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHospitalsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name equals to DEFAULT_NAME
        defaultHospitalShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name not equals to DEFAULT_NAME
        defaultHospitalShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the hospitalList where name not equals to UPDATED_NAME
        defaultHospitalShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHospitalShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name is not null
        defaultHospitalShouldBeFound("name.specified=true");

        // Get all the hospitalList where name is null
        defaultHospitalShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllHospitalsByNameContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name contains DEFAULT_NAME
        defaultHospitalShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the hospitalList where name contains UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHospitalsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name does not contain DEFAULT_NAME
        defaultHospitalShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the hospitalList where name does not contain UPDATED_NAME
        defaultHospitalShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllHospitalsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where phone equals to DEFAULT_PHONE
        defaultHospitalShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the hospitalList where phone equals to UPDATED_PHONE
        defaultHospitalShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where phone not equals to DEFAULT_PHONE
        defaultHospitalShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the hospitalList where phone not equals to UPDATED_PHONE
        defaultHospitalShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultHospitalShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the hospitalList where phone equals to UPDATED_PHONE
        defaultHospitalShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where phone is not null
        defaultHospitalShouldBeFound("phone.specified=true");

        // Get all the hospitalList where phone is null
        defaultHospitalShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllHospitalsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where phone contains DEFAULT_PHONE
        defaultHospitalShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the hospitalList where phone contains UPDATED_PHONE
        defaultHospitalShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHospitalsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where phone does not contain DEFAULT_PHONE
        defaultHospitalShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the hospitalList where phone does not contain UPDATED_PHONE
        defaultHospitalShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllHospitalsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address equals to DEFAULT_ADDRESS
        defaultHospitalShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address equals to UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHospitalsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address not equals to DEFAULT_ADDRESS
        defaultHospitalShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address not equals to UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHospitalsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the hospitalList where address equals to UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHospitalsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address is not null
        defaultHospitalShouldBeFound("address.specified=true");

        // Get all the hospitalList where address is null
        defaultHospitalShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllHospitalsByAddressContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address contains DEFAULT_ADDRESS
        defaultHospitalShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address contains UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHospitalsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address does not contain DEFAULT_ADDRESS
        defaultHospitalShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address does not contain UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllHospitalsByPaidForIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where paidFor equals to DEFAULT_PAID_FOR
        defaultHospitalShouldBeFound("paidFor.equals=" + DEFAULT_PAID_FOR);

        // Get all the hospitalList where paidFor equals to UPDATED_PAID_FOR
        defaultHospitalShouldNotBeFound("paidFor.equals=" + UPDATED_PAID_FOR);
    }

    @Test
    @Transactional
    public void getAllHospitalsByPaidForIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where paidFor not equals to DEFAULT_PAID_FOR
        defaultHospitalShouldNotBeFound("paidFor.notEquals=" + DEFAULT_PAID_FOR);

        // Get all the hospitalList where paidFor not equals to UPDATED_PAID_FOR
        defaultHospitalShouldBeFound("paidFor.notEquals=" + UPDATED_PAID_FOR);
    }

    @Test
    @Transactional
    public void getAllHospitalsByPaidForIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where paidFor in DEFAULT_PAID_FOR or UPDATED_PAID_FOR
        defaultHospitalShouldBeFound("paidFor.in=" + DEFAULT_PAID_FOR + "," + UPDATED_PAID_FOR);

        // Get all the hospitalList where paidFor equals to UPDATED_PAID_FOR
        defaultHospitalShouldNotBeFound("paidFor.in=" + UPDATED_PAID_FOR);
    }

    @Test
    @Transactional
    public void getAllHospitalsByPaidForIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where paidFor is not null
        defaultHospitalShouldBeFound("paidFor.specified=true");

        // Get all the hospitalList where paidFor is null
        defaultHospitalShouldNotBeFound("paidFor.specified=false");
    }

    @Test
    @Transactional
    public void getAllHospitalsByOncologistsIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);
        Oncologist oncologists = OncologistResourceIT.createEntity(em);
        em.persist(oncologists);
        em.flush();
        hospital.addOncologists(oncologists);
        hospitalRepository.saveAndFlush(hospital);
        Long oncologistsId = oncologists.getId();

        // Get all the hospitalList where oncologists equals to oncologistsId
        defaultHospitalShouldBeFound("oncologistsId.equals=" + oncologistsId);

        // Get all the hospitalList where oncologists equals to oncologistsId + 1
        defaultHospitalShouldNotBeFound("oncologistsId.equals=" + (oncologistsId + 1));
    }


    @Test
    @Transactional
    public void getAllHospitalsByTherapistsIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);
        Therapist therapists = TherapistResourceIT.createEntity(em);
        em.persist(therapists);
        em.flush();
        hospital.addTherapists(therapists);
        hospitalRepository.saveAndFlush(hospital);
        Long therapistsId = therapists.getId();

        // Get all the hospitalList where therapists equals to therapistsId
        defaultHospitalShouldBeFound("therapistsId.equals=" + therapistsId);

        // Get all the hospitalList where therapists equals to therapistsId + 1
        defaultHospitalShouldNotBeFound("therapistsId.equals=" + (therapistsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHospitalShouldBeFound(String filter) throws Exception {
        restHospitalMockMvc.perform(get("/api/hospitals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].paidFor").value(hasItem(DEFAULT_PAID_FOR.booleanValue())));

        // Check, that the count call also returns 1
        restHospitalMockMvc.perform(get("/api/hospitals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHospitalShouldNotBeFound(String filter) throws Exception {
        restHospitalMockMvc.perform(get("/api/hospitals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHospitalMockMvc.perform(get("/api/hospitals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHospital() throws Exception {
        // Get the hospital
        restHospitalMockMvc.perform(get("/api/hospitals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHospital() throws Exception {
        // Initialize the database
        hospitalService.save(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital
        Hospital updatedHospital = hospitalRepository.findById(hospital.getId()).get();
        // Disconnect from session so that the updates on updatedHospital are not directly saved in db
        em.detach(updatedHospital);
        updatedHospital
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .paidFor(UPDATED_PAID_FOR);

        restHospitalMockMvc.perform(put("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHospital)))
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testHospital.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHospital.isPaidFor()).isEqualTo(UPDATED_PAID_FOR);
    }

    @Test
    @Transactional
    public void updateNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Create the Hospital

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc.perform(put("/api/hospitals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHospital() throws Exception {
        // Initialize the database
        hospitalService.save(hospital);

        int databaseSizeBeforeDelete = hospitalRepository.findAll().size();

        // Delete the hospital
        restHospitalMockMvc.perform(delete("/api/hospitals/{id}", hospital.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
