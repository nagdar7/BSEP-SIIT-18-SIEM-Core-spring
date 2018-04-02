package com.siemcore.repository;

import com.siemcore.domain.AlarmHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the AlarmHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlarmHistoryRepository extends MongoRepository<AlarmHistory, String> {

}
