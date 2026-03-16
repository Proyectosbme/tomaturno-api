package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.input.dto.TurnoResponseDTO;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.vo.EstadoTurno;

@Mapper(componentModel = "cdi")
public interface TurnoInputMapper {

    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "estado", target = "descripcionEstado", qualifiedByName = "estadoToDescripcion")
    TurnoResponseDTO toResponse(Turno turno);

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(EstadoTurno estado) {
        return estado != null ? estado.getCodigo() : null;
    }

    @Named("estadoToDescripcion")
    default String estadoToDescripcion(EstadoTurno estado) {
        return estado != null ? estado.getDescripcion() : null;
    }
}
