package com.paneyko.allscript.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.paneyko.allscript.domain.enumeration.TestType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.paneyko.allscript.domain.TestResult} entity. This class is used
 * in {@link com.paneyko.allscript.web.rest.TestResultResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-results?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TestResultCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TestType
     */
    public static class TestTypeFilter extends Filter<TestType> {

        public TestTypeFilter() {
        }

        public TestTypeFilter(TestTypeFilter filter) {
            super(filter);
        }

        @Override
        public TestTypeFilter copy() {
            return new TestTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private TestTypeFilter type;

    private ZonedDateTimeFilter dateTime;

    private StringFilter description;

    private StringFilter url;

    private LongFilter oncologistId;

    private LongFilter patientId;

    public TestResultCriteria() {
    }

    public TestResultCriteria(TestResultCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.dateTime = other.dateTime == null ? null : other.dateTime.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.oncologistId = other.oncologistId == null ? null : other.oncologistId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
    }

    @Override
    public TestResultCriteria copy() {
        return new TestResultCriteria(this);
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

    public TestTypeFilter getType() {
        return type;
    }

    public void setType(TestTypeFilter type) {
        this.type = type;
    }

    public ZonedDateTimeFilter getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTimeFilter dateTime) {
        this.dateTime = dateTime;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public LongFilter getOncologistId() {
        return oncologistId;
    }

    public void setOncologistId(LongFilter oncologistId) {
        this.oncologistId = oncologistId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TestResultCriteria that = (TestResultCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(dateTime, that.dateTime) &&
            Objects.equals(description, that.description) &&
            Objects.equals(url, that.url) &&
            Objects.equals(oncologistId, that.oncologistId) &&
            Objects.equals(patientId, that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        type,
        dateTime,
        description,
        url,
        oncologistId,
        patientId
        );
    }

    @Override
    public String toString() {
        return "TestResultCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (dateTime != null ? "dateTime=" + dateTime + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (oncologistId != null ? "oncologistId=" + oncologistId + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
            "}";
    }

}
