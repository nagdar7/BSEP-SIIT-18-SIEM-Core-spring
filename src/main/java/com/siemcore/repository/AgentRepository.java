package com.siemcore.repository;

import com.siemcore.domain.Agent;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Agent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentRepository extends MongoRepository<Agent, String> {

}
