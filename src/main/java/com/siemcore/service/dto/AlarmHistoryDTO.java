package com.siemcore.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AlarmHistory entity.
 */
public class AlarmHistoryDTO implements Serializable {

    private String id;

    private ZonedDateTime date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlarmHistoryDTO alarmHistoryDTO = (AlarmHistoryDTO) o;
        if(alarmHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alarmHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlarmHistoryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
