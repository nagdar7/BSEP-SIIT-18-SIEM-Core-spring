package com.siemcore.service.mapper;

import com.siemcore.domain.*;
import com.siemcore.service.dto.AlarmRuleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AlarmRule and its DTO AlarmRuleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlarmRuleMapper extends EntityMapper<AlarmRuleDTO, AlarmRule> {

    

    
}
