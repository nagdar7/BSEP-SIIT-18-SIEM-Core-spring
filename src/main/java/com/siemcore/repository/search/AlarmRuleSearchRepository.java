package com.siemcore.repository.search;

import com.siemcore.domain.AlarmRule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AlarmRule entity.
 */
public interface AlarmRuleSearchRepository extends ElasticsearchRepository<AlarmRule, String> {
}
