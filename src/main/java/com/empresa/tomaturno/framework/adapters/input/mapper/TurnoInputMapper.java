package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;
import com.empresa.tomaturno.framework.adapters.input.dto.TurnoResponseDTO;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

@Mapper(componentModel = "cdi")
public interface TurnoInputMapper {

    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCorrelativo")
    @Mapping(source = "estado", target = "descripcionEstado", qualifiedByName = "estadoToNombre")
    TurnoResponseDTO toResponse(Turno turno);

    @Named("estadoToCorrelativo")
    default Integer estadoToCorrelativo(CatalogoDetalle estado) {
        return estado != null ? estado.getCorrelativo() : null;
    }

    @Named("estadoToNombre")
    default String estadoToNombre(CatalogoDetalle estado) {
        return estado != null ? estado.getNombre() : null;
    }
}
