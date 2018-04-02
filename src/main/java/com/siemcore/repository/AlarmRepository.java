package com.siemcore.repository;

import com.siemcore.domain.Alarm;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Alarm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlarmRepository extends MongoRepository<Alarm, String> {

}
