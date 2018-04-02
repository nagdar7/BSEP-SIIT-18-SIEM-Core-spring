package com.siemcore.service.mapper;

import com.siemcore.domain.*;
import com.siemcore.service.dto.AlarmHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AlarmHistory and its DTO AlarmHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlarmHistoryMapper extends EntityMapper<AlarmHistoryDTO, AlarmHistory> {

    

    
}
