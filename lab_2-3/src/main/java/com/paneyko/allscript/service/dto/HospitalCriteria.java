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
 * Criteria class for the {@link com.paneyko.allscript.domain.Hospital} entity. This class is used
 * in {@link com.paneyko.allscript.web.rest.HospitalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hospitals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HospitalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter phone;

    private StringFilter address;

    private BooleanFilter paidFor;

    private LongFilter oncologistsId;

    private LongFilter therapistsId;

    public HospitalCriteria() {
    }

    public HospitalCriteria(HospitalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.paidFor = other.paidFor == null ? null : other.paidFor.copy();
        this.oncologistsId = other.oncologistsId == null ? null : other.oncologistsId.copy();
        this.therapistsId = other.therapistsId == null ? null : other.therapistsId.copy();
    }

    @Override
    public HospitalCriteria copy() {
        return new HospitalCriteria(this);
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

    public BooleanFilter getPaidFor() {
        return paidFor;
    }

    public void setPaidFor(BooleanFilter paidFor) {
        this.paidFor = paidFor;
    }

    public LongFilter getOncologistsId() {
        return oncologistsId;
    }

    public void setOncologistsId(LongFilter oncologistsId) {
        this.oncologistsId = oncologistsId;
    }

    public LongFilter getTherapistsId() {
        return therapistsId;
    }

    public void setTherapistsId(LongFilter therapistsId) {
        this.therapistsId = therapistsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HospitalCriteria that = (HospitalCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(address, that.address) &&
            Objects.equals(paidFor, that.paidFor) &&
            Objects.equals(oncologistsId, that.oncologistsId) &&
            Objects.equals(therapistsId, that.therapistsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        phone,
        address,
        paidFor,
        oncologistsId,
        therapistsId
        );
    }

    @Override
    public String toString() {
        return "HospitalCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (paidFor != null ? "paidFor=" + paidFor + ", " : "") +
                (oncologistsId != null ? "oncologistsId=" + oncologistsId + ", " : "") +
                (therapistsId != null ? "therapistsId=" + therapistsId + ", " : "") +
            "}";
    }

}
