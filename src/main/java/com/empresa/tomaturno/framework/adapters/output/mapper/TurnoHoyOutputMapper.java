package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.VwTurnosHoyJpaEntity;
import com.empresa.tomaturno.turno.application.query.dto.TurnoHoyDTO;

@Mapper(componentModel = "cdi")
public interface TurnoHoyOutputMapper {

    default TurnoHoyDTO toDomain(VwTurnosHoyJpaEntity e) {
        if (e == null)
            return null;
        return new TurnoHoyDTO(
                e.getId(),
                e.getCodigoTurno(),
                e.getIdSucursalTicket(),
                e.getSucursalTicket(),
                e.getIdUsuario(),
                e.getNombreCompleto(),
                e.getCodigoUsuario(),
                e.getIdPuesto(),
                e.getIdPuestoSucursal(),
                e.getPuesto(),
                e.getIdCola(),
                e.getCola(),
                e.getIdDetalle(),
                e.getDetalle(),
                e.getTipoCasoEspecial(),
                e.getCasoEspecial(),
                e.getFechaCreacion(),
                e.getFechaLlamada(),
                e.getFechaFinalizacion(),
                e.getIdCatalogoEstado(),
                e.getIdCatalogoEstadoDetalle(),
                e.getEstadoTurno(),
                e.getIdTurnoRelacionado());
    }
}
