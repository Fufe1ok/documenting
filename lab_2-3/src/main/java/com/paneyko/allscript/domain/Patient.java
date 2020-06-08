package com.paneyko.allscript.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
public class Patient implements Serializable {

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

    @Column(name = "age")
    private Integer age;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    @Column(name = "phone", unique = true)
    private String phone;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @OneToMany(mappedBy = "forPatient")
    private Set<Conclusion> conclusions = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<TestResult> tests = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "patient_therapists",
               joinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "therapists_id", referencedColumnName = "id"))
    private Set<Therapist> therapists = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "patient_oncologists",
               joinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "oncologists_id", referencedColumnName = "id"))
    private Set<Oncologist> oncologists = new HashSet<>();

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

    public Patient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Patient surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public Patient email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public Patient age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public Patient weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public Patient height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getPhone() {
        return phone;
    }

    public Patient phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Patient address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Conclusion> getConclusions() {
        return conclusions;
    }

    public Patient conclusions(Set<Conclusion> conclusions) {
        this.conclusions = conclusions;
        return this;
    }

    public Patient addConclusions(Conclusion conclusion) {
        this.conclusions.add(conclusion);
        conclusion.setForPatient(this);
        return this;
    }

    public Patient removeConclusions(Conclusion conclusion) {
        this.conclusions.remove(conclusion);
        conclusion.setForPatient(null);
        return this;
    }

    public void setConclusions(Set<Conclusion> conclusions) {
        this.conclusions = conclusions;
    }

    public Set<TestResult> getTests() {
        return tests;
    }

    public Patient tests(Set<TestResult> testResults) {
        this.tests = testResults;
        return this;
    }

    public Patient addTests(TestResult testResult) {
        this.tests.add(testResult);
        testResult.setPatient(this);
        return this;
    }

    public Patient removeTests(TestResult testResult) {
        this.tests.remove(testResult);
        testResult.setPatient(null);
        return this;
    }

    public void setTests(Set<TestResult> testResults) {
        this.tests = testResults;
    }

    public Set<Therapist> getTherapists() {
        return therapists;
    }

    public Patient therapists(Set<Therapist> therapists) {
        this.therapists = therapists;
        return this;
    }

    public Patient addTherapists(Therapist therapist) {
        this.therapists.add(therapist);
        therapist.getPatients().add(this);
        return this;
    }

    public Patient removeTherapists(Therapist therapist) {
        this.therapists.remove(therapist);
        therapist.getPatients().remove(this);
        return this;
    }

    public void setTherapists(Set<Therapist> therapists) {
        this.therapists = therapists;
    }

    public Set<Oncologist> getOncologists() {
        return oncologists;
    }

    public Patient oncologists(Set<Oncologist> oncologists) {
        this.oncologists = oncologists;
        return this;
    }

    public Patient addOncologists(Oncologist oncologist) {
        this.oncologists.add(oncologist);
        oncologist.getPatients().add(this);
        return this;
    }

    public Patient removeOncologists(Oncologist oncologist) {
        this.oncologists.remove(oncologist);
        oncologist.getPatients().remove(this);
        return this;
    }

    public void setOncologists(Set<Oncologist> oncologists) {
        this.oncologists = oncologists;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", age=" + getAge() +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
