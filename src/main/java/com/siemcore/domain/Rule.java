package com.siemcore.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Rule.
 */
@Document(collection = "rule")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rule")
public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("rule")
    private String rule;

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

    public Rule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public Rule rule(String rule) {
        this.rule = rule;
        return this;
    }

    public void setRule(String rule) {
        this.rule = rule;
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
        Rule rule = (Rule) o;
        if (rule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rule='" + getRule() + "'" +
            "}";
    }
}
