package com.siemcore.repository;

import com.siemcore.domain.Rule;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Rule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RuleRepository extends MongoRepository<Rule, String> {

}
