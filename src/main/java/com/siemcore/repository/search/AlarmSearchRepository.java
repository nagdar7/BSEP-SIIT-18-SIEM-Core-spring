package com.siemcore.repository.search;

import com.siemcore.domain.Alarm;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Alarm entity.
 */
public interface AlarmSearchRepository extends ElasticsearchRepository<Alarm, String> {
}
