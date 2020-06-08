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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.paneyko.allscript.domain.Conclusion} entity. This class is used
 * in {@link com.paneyko.allscript.web.rest.ConclusionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conclusions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConclusionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter date;

    private StringFilter resultDescription;

    private StringFilter url;

    private LongFilter signedById;

    private LongFilter forPatientId;

    public ConclusionCriteria() {
    }

    public ConclusionCriteria(ConclusionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.resultDescription = other.resultDescription == null ? null : other.resultDescription.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.signedById = other.signedById == null ? null : other.signedById.copy();
        this.forPatientId = other.forPatientId == null ? null : other.forPatientId.copy();
    }

    @Override
    public ConclusionCriteria copy() {
        return new ConclusionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public StringFilter getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(StringFilter resultDescription) {
        this.resultDescription = resultDescription;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public LongFilter getSignedById() {
        return signedById;
    }

    public void setSignedById(LongFilter signedById) {
        this.signedById = signedById;
    }

    public LongFilter getForPatientId() {
        return forPatientId;
    }

    public void setForPatientId(LongFilter forPatientId) {
        this.forPatientId = forPatientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConclusionCriteria that = (ConclusionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(resultDescription, that.resultDescription) &&
            Objects.equals(url, that.url) &&
            Objects.equals(signedById, that.signedById) &&
            Objects.equals(forPatientId, that.forPatientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        resultDescription,
        url,
        signedById,
        forPatientId
        );
    }

    @Override
    public String toString() {
        return "ConclusionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (resultDescription != null ? "resultDescription=" + resultDescription + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (signedById != null ? "signedById=" + signedById + ", " : "") +
                (forPatientId != null ? "forPatientId=" + forPatientId + ", " : "") +
            "}";
    }

}
