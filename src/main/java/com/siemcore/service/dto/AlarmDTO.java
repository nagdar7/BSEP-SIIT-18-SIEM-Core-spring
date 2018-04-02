package com.siemcore.service.dto;


import java.io.Serializable;
import java.util.Objects;
import com.siemcore.domain.enumeration.ALARM_STATUS;

/**
 * A DTO for the Alarm entity.
 */
public class AlarmDTO implements Serializable {

    private String id;

    private String name;

    private ALARM_STATUS status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ALARM_STATUS getStatus() {
        return status;
    }

    public void setStatus(ALARM_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlarmDTO alarmDTO = (AlarmDTO) o;
        if(alarmDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alarmDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlarmDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
