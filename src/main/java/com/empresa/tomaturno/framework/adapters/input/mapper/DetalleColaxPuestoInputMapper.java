package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoResponseDTO;

@Mapper(componentModel = "cdi")
public interface DetalleColaxPuestoInputMapper {

    default DetalleColaxPuesto toDomain(DetalleColaxPuestoRequestDTO dto) {
        return DetalleColaxPuesto.inicializar(
                dto.getIdPuesto(),
                dto.getIdSucursalPuesto(),
                dto.getIdCola(),
                dto.getIdDetalle(),
                dto.getIdSucursalCola(),
                dto.getPrioridad());
    }

    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    DetalleColaxPuestoResponseDTO toResponse(DetalleColaxPuesto domain);
}
