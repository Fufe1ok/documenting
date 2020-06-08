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
 * Criteria class for the {@link com.paneyko.allscript.domain.Oncologist} entity. This class is used
 * in {@link com.paneyko.allscript.web.rest.OncologistResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /oncologists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OncologistCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter surname;

    private StringFilter email;

    private StringFilter phone;

    private IntegerFilter roomIn;

    private LongFilter conclusionsId;

    private LongFilter testsId;

    private LongFilter hospitalId;

    private LongFilter patientsId;

    public OncologistCriteria() {
    }

    public OncologistCriteria(OncologistCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.surname = other.surname == null ? null : other.surname.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.roomIn = other.roomIn == null ? null : other.roomIn.copy();
        this.conclusionsId = other.conclusionsId == null ? null : other.conclusionsId.copy();
        this.testsId = other.testsId == null ? null : other.testsId.copy();
        this.hospitalId = other.hospitalId == null ? null : other.hospitalId.copy();
        this.patientsId = other.patientsId == null ? null : other.patientsId.copy();
    }

    @Override
    public OncologistCriteria copy() {
        return new OncologistCriteria(this);
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

    public LongFilter getConclusionsId() {
        return conclusionsId;
    }

    public void setConclusionsId(LongFilter conclusionsId) {
        this.conclusionsId = conclusionsId;
    }

    public LongFilter getTestsId() {
        return testsId;
    }

    public void setTestsId(LongFilter testsId) {
        this.testsId = testsId;
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
        final OncologistCriteria that = (OncologistCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(roomIn, that.roomIn) &&
            Objects.equals(conclusionsId, that.conclusionsId) &&
            Objects.equals(testsId, that.testsId) &&
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
        conclusionsId,
        testsId,
        hospitalId,
        patientsId
        );
    }

    @Override
    public String toString() {
        return "OncologistCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (surname != null ? "surname=" + surname + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (roomIn != null ? "roomIn=" + roomIn + ", " : "") +
                (conclusionsId != null ? "conclusionsId=" + conclusionsId + ", " : "") +
                (testsId != null ? "testsId=" + testsId + ", " : "") +
                (hospitalId != null ? "hospitalId=" + hospitalId + ", " : "") +
                (patientsId != null ? "patientsId=" + patientsId + ", " : "") +
            "}";
    }

}
