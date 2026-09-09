package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;

import com.empresa.tomaturno.framework.adapters.input.dto.TurnoHoyResponseDTO;
import com.empresa.tomaturno.turno.application.query.dto.TurnoHoyDTO;

@Mapper(componentModel = "cdi")
public interface TurnoHoyInputMapper {
    TurnoHoyResponseDTO toResponse(TurnoHoyDTO turnoHoy);
}
