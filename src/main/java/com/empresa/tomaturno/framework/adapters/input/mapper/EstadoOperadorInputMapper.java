package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.framework.adapters.input.dto.EstadoOperadorResponseDTO;

@Mapper(componentModel = "cdi")
public interface EstadoOperadorInputMapper {

    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    EstadoOperadorResponseDTO toResponse(EstadoOperador estadoOperador);
}
