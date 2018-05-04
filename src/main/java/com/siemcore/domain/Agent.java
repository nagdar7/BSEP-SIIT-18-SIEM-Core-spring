package com.siemcore.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Agent.
 */
@Document(collection = "agent")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "agent")
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("directory")
    private String directory;

    @Field("description")
    private String description;

    @Field("filter_expression")
    private String filterExpression;

    @NotNull
    @Field("api_key")
    private String apiKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirectory() {
        return directory;
    }

    public Agent directory(String directory) {
        this.directory = directory;
        return this;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDescription() {
        return description;
    }

    public Agent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public Agent filterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
        return this;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Agent apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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
        Agent agent = (Agent) o;
        if (agent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Agent{" +
            "id=" + getId() +
            ", directory='" + getDirectory() + "'" +
            ", description='" + getDescription() + "'" +
            ", filterExpression='" + getFilterExpression() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            "}";
    }
}
