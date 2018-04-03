package com.siemcore.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Agent entity.
 */
public class AgentDTO implements Serializable {

    private String id;

    private String directory;

    private String description;

    private String filterExpression;

    @NotNull
    private String api_key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgentDTO agentDTO = (AgentDTO) o;
        if(agentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgentDTO{" +
            "id=" + getId() +
            ", directory='" + getDirectory() + "'" +
            ", description='" + getDescription() + "'" +
            ", filterExpression='" + getFilterExpression() + "'" +
            ", api_key='" + getApi_key() + "'" +
            "}";
    }
}
