package com.siemcore.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Log.
 */
@Document(collection = "log")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("facility")
    private Long facility;

    @Field("severnity")
    private Long severnity;

    @Field("message")
    private String message;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getFacility() {
        return facility;
    }

    public Log facility(Long facility) {
        this.facility = facility;
        return this;
    }

    public void setFacility(Long facility) {
        this.facility = facility;
    }

    public Long getSevernity() {
        return severnity;
    }

    public Log severnity(Long severnity) {
        this.severnity = severnity;
        return this;
    }

    public void setSevernity(Long severnity) {
        this.severnity = severnity;
    }

    public String getMessage() {
        return message;
    }

    public Log message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Log log = (Log) o;
        if (log.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), log.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Log{" +
            "id=" + getId() +
            ", facility=" + getFacility() +
            ", severnity=" + getSevernity() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
