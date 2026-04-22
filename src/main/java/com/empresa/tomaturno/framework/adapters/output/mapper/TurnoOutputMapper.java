package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

@Mapper(componentModel = "cdi")
public interface TurnoOutputMapper {

    @Mapping(target = "idpk.idSucursal", source = "idSucursal")
    @Mapping(target = "idpk.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "idpk.codigoTurno", source = "codigoTurno")
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "codigoTurno", ignore = true)
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCorrelativo")
    @Mapping(target = "idCatalogoEstado", source = "estado", qualifiedByName = "estadoToIdCatalogo")
    TurnoJpaEntity toJpaEntity(Turno turno);

    default Turno toDomain(TurnoJpaEntity entity) {
        CatalogoDetalle estado = null;
        if (entity.getEstado() != null) {
            estado = CatalogoDetalle.conCorrelativo(entity.getEstado());
            if (entity.getIdCatalogoEstado() != null) {
                estado.asignarIdCatalogo(entity.getIdCatalogoEstado());
            }
        }
        return Turno.builder()
                .id(entity.getId())
                .idSucursal(entity.getIdpk().getIdSucursal())
                .fechaCreacion(entity.getIdpk().getFechaCreacion())
                .codigoTurno(entity.getIdpk().getCodigoTurno())
                .fechaLlamada(entity.getFechaLlamada())
                .fechaFinalizacion(entity.getFechaFinalizacion())
                .idCola(entity.getIdCola())
                .idDetalle(entity.getIdDetalle())
                .estado(estado)
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
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCorrelativo")
    @Mapping(target = "idCatalogoEstado", source = "estado", qualifiedByName = "estadoToIdCatalogo")
    void updateEntityFromDomain(Turno turno, @MappingTarget TurnoJpaEntity entity);

    @Named("estadoToCorrelativo")
    static Integer estadoToCorrelativo(CatalogoDetalle estado) {
        return estado == null ? null : estado.getCorrelativo();
    }

    @Named("estadoToIdCatalogo")
    static Long estadoToIdCatalogo(CatalogoDetalle estado) {
        return estado == null ? null : estado.getIdCatalogo();
    }
}
