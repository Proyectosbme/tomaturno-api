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

    @Mapping(target = "idSucursal", source = "idpk.idSucursal")
    @Mapping(target = "fechaCreacion", source = "idpk.fechaCreacion")
    @Mapping(target = "codigoTurno", source = "idpk.codigoTurno")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    Turno toDomain(TurnoJpaEntity entity);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "codigoTurno", ignore = true)
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Turno turno, @MappingTarget TurnoJpaEntity entity);

    @Named("integerToEstado")
    static EstadoTurno integerToEstado(Integer codigo) {
        return codigo == null ? null : EstadoTurno.fromCodigo(codigo);
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(EstadoTurno estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
