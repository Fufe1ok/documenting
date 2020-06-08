package com.paneyko.allscript.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Oncologist.
 */
@Entity
@Table(name = "oncologist")
public class Oncologist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(max = 100)
    @Column(name = "surname", length = 100, nullable = false)
    private String surname;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "room_in")
    private Integer roomIn;

    @OneToMany(mappedBy = "signedBy")
    private Set<Conclusion> conclusions = new HashSet<>();

    @OneToMany(mappedBy = "oncologist")
    private Set<TestResult> tests = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("oncologists")
    private Hospital hospital;

    @ManyToMany(mappedBy = "oncologists")
    @JsonIgnore
    private Set<Patient> patients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Oncologist name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Oncologist surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public Oncologist email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Oncologist phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoomIn() {
        return roomIn;
    }

    public Oncologist roomIn(Integer roomIn) {
        this.roomIn = roomIn;
        return this;
    }

    public void setRoomIn(Integer roomIn) {
        this.roomIn = roomIn;
    }

    public Set<Conclusion> getConclusions() {
        return conclusions;
    }

    public Oncologist conclusions(Set<Conclusion> conclusions) {
        this.conclusions = conclusions;
        return this;
    }

    public Oncologist addConclusions(Conclusion conclusion) {
        this.conclusions.add(conclusion);
        conclusion.setSignedBy(this);
        return this;
    }

    public Oncologist removeConclusions(Conclusion conclusion) {
        this.conclusions.remove(conclusion);
        conclusion.setSignedBy(null);
        return this;
    }

    public void setConclusions(Set<Conclusion> conclusions) {
        this.conclusions = conclusions;
    }

    public Set<TestResult> getTests() {
        return tests;
    }

    public Oncologist tests(Set<TestResult> testResults) {
        this.tests = testResults;
        return this;
    }

    public Oncologist addTests(TestResult testResult) {
        this.tests.add(testResult);
        testResult.setOncologist(this);
        return this;
    }

    public Oncologist removeTests(TestResult testResult) {
        this.tests.remove(testResult);
        testResult.setOncologist(null);
        return this;
    }

    public void setTests(Set<TestResult> testResults) {
        this.tests = testResults;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public Oncologist hospital(Hospital hospital) {
        this.hospital = hospital;
        return this;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public Oncologist patients(Set<Patient> patients) {
        this.patients = patients;
        return this;
    }

    public Oncologist addPatients(Patient patient) {
        this.patients.add(patient);
        patient.getOncologists().add(this);
        return this;
    }

    public Oncologist removePatients(Patient patient) {
        this.patients.remove(patient);
        patient.getOncologists().remove(this);
        return this;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Oncologist)) {
            return false;
        }
        return id != null && id.equals(((Oncologist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Oncologist{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", roomIn=" + getRoomIn() +
            "}";
    }
}
