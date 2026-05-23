package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.TurnoJpaEntity;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.vo.CatalogoDetalle;

@Mapper(componentModel = "cdi")
public interface TurnoOutputMapper {

    @Mapping(target = "idpk.idSucursal",  source = "idSucursal")
    @Mapping(target = "idpk.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "idpk.codigoTurno", source = "codigoTurno")
    @Mapping(target = "idSucursal",  ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "codigoTurno", ignore = true)
    @Mapping(target = "idCatalogoEstado",
             expression = "java(turno.getEstado() != null ? turno.getEstado().catalogo() : null)")
    @Mapping(target = "idCatalogoEstadoDetalle",
             expression = "java(turno.getEstado() != null ? turno.getEstado().detalle() : null)")
    TurnoJpaEntity toJpaEntity(Turno turno);

    default Turno toDomain(TurnoJpaEntity entity) {
        CatalogoDetalle estado = null;
        if (entity.getIdCatalogoEstado() != null && entity.getIdCatalogoEstadoDetalle() != null) {
            estado = new CatalogoDetalle(entity.getIdCatalogoEstado(), entity.getIdCatalogoEstadoDetalle());
        } else if (entity.getIdCatalogoEstadoDetalle() != null) {
            // compatibilidad con registros anteriores sin idCatalogoEstadoDetalle
            estado = new CatalogoDetalle(
                entity.getIdCatalogoEstado() != null ? entity.getIdCatalogoEstado() : 0L,
                entity.getIdCatalogoEstadoDetalle().longValue());
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

    @Mapping(target = "idpk",        ignore = true)
    @Mapping(target = "idSucursal",  ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "codigoTurno", ignore = true)
    @Mapping(target = "idCatalogoEstado",
             expression = "java(turno.getEstado() != null ? turno.getEstado().catalogo() : null)")
    @Mapping(target = "idCatalogoEstadoDetalle",
             expression = "java(turno.getEstado() != null ? turno.getEstado().detalle() : null)")
    void updateEntityFromDomain(Turno turno, @MappingTarget TurnoJpaEntity entity);
}
