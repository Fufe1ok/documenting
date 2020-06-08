package com.paneyko.allscript.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.paneyko.allscript.domain.Therapist} entity. This class is used
 * in {@link com.paneyko.allscript.web.rest.TherapistResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /therapists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TherapistCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter surname;

    private StringFilter email;

    private StringFilter phone;

    private IntegerFilter roomIn;

    private LongFilter hospitalId;

    private LongFilter patientsId;

    public TherapistCriteria() {
    }

    public TherapistCriteria(TherapistCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.surname = other.surname == null ? null : other.surname.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.roomIn = other.roomIn == null ? null : other.roomIn.copy();
        this.hospitalId = other.hospitalId == null ? null : other.hospitalId.copy();
        this.patientsId = other.patientsId == null ? null : other.patientsId.copy();
    }

    @Override
    public TherapistCriteria copy() {
        return new TherapistCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSurname() {
        return surname;
    }

    public void setSurname(StringFilter surname) {
        this.surname = surname;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public IntegerFilter getRoomIn() {
        return roomIn;
    }

    public void setRoomIn(IntegerFilter roomIn) {
        this.roomIn = roomIn;
    }

    public LongFilter getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(LongFilter hospitalId) {
        this.hospitalId = hospitalId;
    }

    public LongFilter getPatientsId() {
        return patientsId;
    }

    public void setPatientsId(LongFilter patientsId) {
        this.patientsId = patientsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TherapistCriteria that = (TherapistCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(roomIn, that.roomIn) &&
            Objects.equals(hospitalId, that.hospitalId) &&
            Objects.equals(patientsId, that.patientsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        surname,
        email,
        phone,
        roomIn,
        hospitalId,
        patientsId
        );
    }

    @Override
    public String toString() {
        return "TherapistCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (surname != null ? "surname=" + surname + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (roomIn != null ? "roomIn=" + roomIn + ", " : "") +
                (hospitalId != null ? "hospitalId=" + hospitalId + ", " : "") +
                (patientsId != null ? "patientsId=" + patientsId + ", " : "") +
            "}";
    }

}
