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
 * Criteria class for the {@link com.paneyko.allscript.domain.Patient} entity. This class is used
 * in {@link com.paneyko.allscript.web.rest.PatientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter surname;

    private StringFilter email;

    private IntegerFilter age;

    private DoubleFilter weight;

    private DoubleFilter height;

    private StringFilter phone;

    private StringFilter address;

    private LongFilter conclusionsId;

    private LongFilter testsId;

    private LongFilter therapistsId;

    private LongFilter oncologistsId;

    public PatientCriteria() {
    }

    public PatientCriteria(PatientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.surname = other.surname == null ? null : other.surname.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.conclusionsId = other.conclusionsId == null ? null : other.conclusionsId.copy();
        this.testsId = other.testsId == null ? null : other.testsId.copy();
        this.therapistsId = other.therapistsId == null ? null : other.therapistsId.copy();
        this.oncologistsId = other.oncologistsId == null ? null : other.oncologistsId.copy();
    }

    @Override
    public PatientCriteria copy() {
        return new PatientCriteria(this);
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

    public IntegerFilter getAge() {
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public DoubleFilter getWeight() {
        return weight;
    }

    public void setWeight(DoubleFilter weight) {
        this.weight = weight;
    }

    public DoubleFilter getHeight() {
        return height;
    }

    public void setHeight(DoubleFilter height) {
        this.height = height;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
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

    public LongFilter getTherapistsId() {
        return therapistsId;
    }

    public void setTherapistsId(LongFilter therapistsId) {
        this.therapistsId = therapistsId;
    }

    public LongFilter getOncologistsId() {
        return oncologistsId;
    }

    public void setOncologistsId(LongFilter oncologistsId) {
        this.oncologistsId = oncologistsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientCriteria that = (PatientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(email, that.email) &&
            Objects.equals(age, that.age) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(height, that.height) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(address, that.address) &&
            Objects.equals(conclusionsId, that.conclusionsId) &&
            Objects.equals(testsId, that.testsId) &&
            Objects.equals(therapistsId, that.therapistsId) &&
            Objects.equals(oncologistsId, that.oncologistsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        surname,
        email,
        age,
        weight,
        height,
        phone,
        address,
        conclusionsId,
        testsId,
        therapistsId,
        oncologistsId
        );
    }

    @Override
    public String toString() {
        return "PatientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (surname != null ? "surname=" + surname + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (age != null ? "age=" + age + ", " : "") +
                (weight != null ? "weight=" + weight + ", " : "") +
                (height != null ? "height=" + height + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (conclusionsId != null ? "conclusionsId=" + conclusionsId + ", " : "") +
                (testsId != null ? "testsId=" + testsId + ", " : "") +
                (therapistsId != null ? "therapistsId=" + therapistsId + ", " : "") +
                (oncologistsId != null ? "oncologistsId=" + oncologistsId + ", " : "") +
            "}";
    }

}
