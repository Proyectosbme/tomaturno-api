package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.vo.Estado;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;

@Mapper(componentModel = "cdi")
public interface ConfiguracionOutputMapper {

    @Mapping(target = "idpk.idConfiguracion", source = "idConfiguracion")
    @Mapping(target = "idpk.idSucursal", source = "idSucursal")
    @Mapping(target = "idConfiguracion", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    ConfiguracionJpaEntity toJpaEntity(Configuracion configuracion);

    @Mapping(target = "idConfiguracion", source = "idpk.idConfiguracion")
    @Mapping(target = "idSucursal", source = "idpk.idSucursal")
    @Mapping(target = "auditoria.usuarioCreacion", source = "userCreacion")
    @Mapping(target = "auditoria.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "auditoria.usuarioModificacion", source = "userModificacion")
    @Mapping(target = "auditoria.fechaModificacion", source = "fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    @Mapping(target = "nombreSucursal", ignore = true)
    Configuracion toDomain(ConfiguracionJpaEntity entity);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "idConfiguracion", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Configuracion configuracion, @MappingTarget ConfiguracionJpaEntity entity);

    @Named("integerToEstado")
    static Estado integerToEstado(Integer codigo) {
        return codigo == null ? null : Estado.fromCodigo(codigo);
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
