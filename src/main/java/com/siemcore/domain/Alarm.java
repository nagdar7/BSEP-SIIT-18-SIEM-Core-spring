package com.siemcore.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

import com.siemcore.domain.enumeration.ALARM_STATUS;

/**
 * A Alarm.
 */
@Document(collection = "alarm")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "alarm")
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("status")
    private ALARM_STATUS status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Alarm name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ALARM_STATUS getStatus() {
        return status;
    }

    public Alarm status(ALARM_STATUS status) {
        this.status = status;
        return this;
    }

    public void setStatus(ALARM_STATUS status) {
        this.status = status;
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
        Alarm alarm = (Alarm) o;
        if (alarm.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alarm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Alarm{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
