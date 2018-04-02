package com.siemcore.repository;

import com.siemcore.domain.Log;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Log entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogRepository extends MongoRepository<Log, String> {

}
