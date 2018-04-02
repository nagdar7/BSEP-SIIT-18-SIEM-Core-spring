package com.siemcore.service.mapper;

import com.siemcore.domain.*;
import com.siemcore.service.dto.AgentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Agent and its DTO AgentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {

    

    
}
