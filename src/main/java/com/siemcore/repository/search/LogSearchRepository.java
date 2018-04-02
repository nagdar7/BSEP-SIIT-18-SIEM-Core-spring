package com.siemcore.repository.search;

import com.siemcore.domain.Log;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Log entity.
 */
public interface LogSearchRepository extends ElasticsearchRepository<Log, String> {
}
