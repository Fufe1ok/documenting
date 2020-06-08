package com.paneyko.allscript.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

/**
 * A Conclusion.
 */
@Entity
@Table(name = "conclusion")
public class Conclusion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "result_description")
    private String resultDescription;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JsonIgnoreProperties("conclusions")
    private Oncologist signedBy;

    @ManyToOne
    @JsonIgnoreProperties("conclusions")
    private Patient forPatient;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Conclusion date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public Conclusion resultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
        return this;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getUrl() {
        return url;
    }

    public Conclusion url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Oncologist getSignedBy() {
        return signedBy;
    }

    public Conclusion signedBy(Oncologist oncologist) {
        this.signedBy = oncologist;
        return this;
    }

    public void setSignedBy(Oncologist oncologist) {
        this.signedBy = oncologist;
    }

    public Patient getForPatient() {
        return forPatient;
    }

    public Conclusion forPatient(Patient patient) {
        this.forPatient = patient;
        return this;
    }

    public void setForPatient(Patient patient) {
        this.forPatient = patient;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conclusion)) {
            return false;
        }
        return id != null && id.equals(((Conclusion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Conclusion{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", resultDescription='" + getResultDescription() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
