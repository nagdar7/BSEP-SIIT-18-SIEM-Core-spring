package com.siemcore.repository;

import com.siemcore.domain.AlarmRule;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the AlarmRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlarmRuleRepository extends MongoRepository<AlarmRule, String> {

}
