package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoResponseDTO;

@Mapper(componentModel = "cdi")
public interface DetalleColaxPuestoInputMapper {

    @Mapping(ignore = true, target = "userCreacion")
    @Mapping(ignore = true, target = "fechaCreacion")
    @Mapping(ignore = true, target = "nombreCola")
    @Mapping(ignore = true, target = "nombreDetalle")
    DetalleColaxPuesto toDomain(DetalleColaxPuestoRequestDTO dto);

    DetalleColaxPuestoResponseDTO toResponse(DetalleColaxPuesto domain);
}
