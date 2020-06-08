package com.paneyko.allscript.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

import com.paneyko.allscript.domain.enumeration.TestType;

/**
 * A TestResult.
 */
@Entity
@Table(name = "test_result")
public class TestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TestType type;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JsonIgnoreProperties("tests")
    private Oncologist oncologist;

    @ManyToOne
    @JsonIgnoreProperties("tests")
    private Patient patient;

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

    public TestResult name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestType getType() {
        return type;
    }

    public TestResult type(TestType type) {
        this.type = type;
        return this;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public TestResult dateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public TestResult description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public TestResult url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Oncologist getOncologist() {
        return oncologist;
    }

    public TestResult oncologist(Oncologist oncologist) {
        this.oncologist = oncologist;
        return this;
    }

    public void setOncologist(Oncologist oncologist) {
        this.oncologist = oncologist;
    }

    public Patient getPatient() {
        return patient;
    }

    public TestResult patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestResult)) {
            return false;
        }
        return id != null && id.equals(((TestResult) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TestResult{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
