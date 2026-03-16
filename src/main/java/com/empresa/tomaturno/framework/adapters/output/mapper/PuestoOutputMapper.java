package com.coop.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.coop.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.coop.tomaturno.puesto.dominio.entity.Puesto;
import com.coop.tomaturno.puesto.dominio.vo.Estado;

@Mapper(componentModel = "cdi")
public interface PuestoOutputMapper {

    @Mapping(target = "idpk.id", source = "identificador")
    @Mapping(target = "idpk.idSucursal", source = "sucursal.identificador")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    PuestoJpaEntity toJpaEntity(Puesto puesto);

    @Mapping(target = "identificador", source = "idpk.id")
    @Mapping(target = "sucursal.identificador", source = "idpk.idSucursal")
    @Mapping(target = "sucursal.nombre", ignore = true)
    @Mapping(target = "auditoria.usuarioCreacion", source = "userCreacion")
    @Mapping(target = "auditoria.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "auditoria.usuarioModificacion", source = "userModificacion")
    @Mapping(target = "auditoria.fechaModificacion", source = "fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    Puesto toDomain(PuestoJpaEntity entity);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Puesto puesto, @MappingTarget PuestoJpaEntity entity);

    @Named("integerToEstado")
    static Estado integerToEstado(Integer codigo) {
        return codigo == null ? null : Estado.fromCodigo(codigo);
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
