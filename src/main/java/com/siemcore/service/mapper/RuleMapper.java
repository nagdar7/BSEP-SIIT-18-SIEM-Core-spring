package com.siemcore.service.mapper;

import com.siemcore.domain.*;
import com.siemcore.service.dto.RuleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Rule and its DTO RuleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RuleMapper extends EntityMapper<RuleDTO, Rule> {

    

    
}
