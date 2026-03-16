package com.coop.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.coop.tomaturno.turno.dominio.entity.Turno;
import com.coop.tomaturno.turno.dominio.vo.EstadoTurno;
import com.coop.tomaturno.framework.adapters.input.dto.TurnoResponseDTO;

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
