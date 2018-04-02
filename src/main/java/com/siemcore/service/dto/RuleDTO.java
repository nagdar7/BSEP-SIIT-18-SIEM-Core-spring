package com.siemcore.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Rule entity.
 */
public class RuleDTO implements Serializable {

    private String id;

    private String name;

    private String rule;

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

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RuleDTO ruleDTO = (RuleDTO) o;
        if(ruleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ruleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rule='" + getRule() + "'" +
            "}";
    }
}
