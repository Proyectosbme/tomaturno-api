package com.empresa.tomaturno.framework.adapters.input.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoResponseDTO;

@Mapper(componentModel = "cdi")
public interface DetalleColaxPuestoInputMapper {

    /** Asignación nueva: la auditoría de creación se arma aquí con el usuario autenticado. */
    default DetalleColaxPuesto toDomain(DetalleColaxPuestoRequestDTO dto, String usuario) {
        return DetalleColaxPuesto.of(new DetalleColaxPuesto.Builder()
                .idPuesto(dto.getIdPuesto())
                .idSucursalPuesto(dto.getIdSucursalPuesto())
                .idCola(dto.getIdCola())
                .idDetalle(dto.getIdDetalle())
                .idSucursalCola(dto.getIdSucursalCola())
                .prioridad(dto.getPrioridad())
                .auditoria(Auditoria.of(usuario, LocalDateTime.now())));
    }

    @Mapping(target = "userCreacion", source = "auditoria.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoria.fecha")
    DetalleColaxPuestoResponseDTO toResponse(DetalleColaxPuesto domain);
}
