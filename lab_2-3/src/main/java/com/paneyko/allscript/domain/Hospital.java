package com.paneyko.allscript.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Hospital.
 */
@Entity
@Table(name = "hospital")
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    @Column(name = "phone", unique = true)
    private String phone;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "paid_for")
    private Boolean paidFor;

    @OneToMany(mappedBy = "hospital")
    private Set<Oncologist> oncologists = new HashSet<>();

    @OneToMany(mappedBy = "hospital")
    private Set<Therapist> therapists = new HashSet<>();

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

    public Hospital name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public Hospital phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Hospital address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isPaidFor() {
        return paidFor;
    }

    public Hospital paidFor(Boolean paidFor) {
        this.paidFor = paidFor;
        return this;
    }

    public void setPaidFor(Boolean paidFor) {
        this.paidFor = paidFor;
    }

    public Set<Oncologist> getOncologists() {
        return oncologists;
    }

    public Hospital oncologists(Set<Oncologist> oncologists) {
        this.oncologists = oncologists;
        return this;
    }

    public Hospital addOncologists(Oncologist oncologist) {
        this.oncologists.add(oncologist);
        oncologist.setHospital(this);
        return this;
    }

    public Hospital removeOncologists(Oncologist oncologist) {
        this.oncologists.remove(oncologist);
        oncologist.setHospital(null);
        return this;
    }

    public void setOncologists(Set<Oncologist> oncologists) {
        this.oncologists = oncologists;
    }

    public Set<Therapist> getTherapists() {
        return therapists;
    }

    public Hospital therapists(Set<Therapist> therapists) {
        this.therapists = therapists;
        return this;
    }

    public Hospital addTherapists(Therapist therapist) {
        this.therapists.add(therapist);
        therapist.setHospital(this);
        return this;
    }

    public Hospital removeTherapists(Therapist therapist) {
        this.therapists.remove(therapist);
        therapist.setHospital(null);
        return this;
    }

    public void setTherapists(Set<Therapist> therapists) {
        this.therapists = therapists;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hospital)) {
            return false;
        }
        return id != null && id.equals(((Hospital) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Hospital{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", paidFor='" + isPaidFor() + "'" +
            "}";
    }
}
