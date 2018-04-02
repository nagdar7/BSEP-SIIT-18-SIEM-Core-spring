package com.siemcore.repository.search;

import com.siemcore.domain.Rule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Rule entity.
 */
public interface RuleSearchRepository extends ElasticsearchRepository<Rule, String> {
}
