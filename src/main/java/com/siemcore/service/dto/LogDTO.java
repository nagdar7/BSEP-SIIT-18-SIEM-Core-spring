package com.siemcore.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Log entity.
 */
public class LogDTO implements Serializable {

    private String id;

    private Long facility;

    private Long severnity;

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getFacility() {
        return facility;
    }

    public void setFacility(Long facility) {
        this.facility = facility;
    }

    public Long getSevernity() {
        return severnity;
    }

    public void setSevernity(Long severnity) {
        this.severnity = severnity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LogDTO logDTO = (LogDTO) o;
        if(logDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), logDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LogDTO{" +
            "id=" + getId() +
            ", facility=" + getFacility() +
            ", severnity=" + getSevernity() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
