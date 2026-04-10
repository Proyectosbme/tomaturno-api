package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.vo.EstadoTurno;

@Mapper(componentModel = "cdi")
public interface TurnoOutputMapper {

    @Mapping(target = "idpk.idSucursal", source = "idSucursal")
    @Mapping(target = "idpk.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "idpk.codigoTurno", source = "codigoTurno")
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "codigoTurno", ignore = true)
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    TurnoJpaEntity toJpaEntity(Turno turno);

    default Turno toDomain(TurnoJpaEntity entity) {
        return Turno.builder()
                .id(entity.getId())
                .idSucursal(entity.getIdpk().getIdSucursal())
                .fechaCreacion(entity.getIdpk().getFechaCreacion())
                .codigoTurno(entity.getIdpk().getCodigoTurno())
                .fechaLlamada(entity.getFechaLlamada())
                .fechaFinalizacion(entity.getFechaFinalizacion())
                .idCola(entity.getIdCola())
                .idDetalle(entity.getIdDetalle())
                .estado(entity.getEstado() != null ? EstadoTurno.fromCodigo(entity.getEstado()) : null)
                .idTurnoRelacionado(entity.getIdTurnoRelacionado())
                .idPuesto(entity.getIdPuesto())
                .idSucursalPuesto(entity.getIdSucursalPuesto())
                .idUsuario(entity.getIdUsuario())
                .idPersona(entity.getIdPersona())
                .tipoCasoEspecial(entity.getTipoCasoEspecial())
                .build();
    }

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "codigoTurno", ignore = true)
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Turno turno, @MappingTarget TurnoJpaEntity entity);

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(EstadoTurno estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
