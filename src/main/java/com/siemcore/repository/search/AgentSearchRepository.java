package com.siemcore.repository.search;

import com.siemcore.domain.Agent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Agent entity.
 */
public interface AgentSearchRepository extends ElasticsearchRepository<Agent, String> {
}
