package com.siemcore.repository.search;

import com.siemcore.domain.AlarmHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AlarmHistory entity.
 */
public interface AlarmHistorySearchRepository extends ElasticsearchRepository<AlarmHistory, String> {
}
