package com.siemcore.service.mapper;

import com.siemcore.domain.*;
import com.siemcore.service.dto.LogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Log and its DTO LogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LogMapper extends EntityMapper<LogDTO, Log> {

    

    
}
