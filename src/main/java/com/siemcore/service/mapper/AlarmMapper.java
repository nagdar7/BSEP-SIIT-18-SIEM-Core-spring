package com.siemcore.service.mapper;

import com.siemcore.domain.*;
import com.siemcore.service.dto.AlarmDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Alarm and its DTO AlarmDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlarmMapper extends EntityMapper<AlarmDTO, Alarm> {

    

    
}
