package com.coop.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.coop.tomaturno.sucursal.dominio.vo.Estado;
import com.coop.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;

@Mapper(componentModel = "cdi")
public interface SucursalOutputMapper {

    @Mapping(target = "id", source = "identificador")
    @Mapping(target = "telefono", source = "contacto.telefono")
    @Mapping(target = "correo", source = "contacto.correo")
    @Mapping(target = "direccion", source = "contacto.direccion")
    @Mapping(target = "usuarioCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "usuarioModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    SucursalJpaEntity toSucursalJpaEntity(Sucursal sucursal);

    @Mapping(target = "identificador", source = "id")
    @Mapping(target = "contacto.telefono", source = "telefono")
    @Mapping(target = "contacto.correo", source = "correo")
    @Mapping(target = "contacto.direccion", source = "direccion")
    @Mapping(target = "auditoria.usuarioCreacion", source = "usuarioCreacion")
    @Mapping(target = "auditoria.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "auditoria.usuarioModificacion", source = "usuarioModificacion")
    @Mapping(target = "auditoria.fechaModificacion", source = "fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    Sucursal toDomain(SucursalJpaEntity sucursalJpaEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "telefono", source = "contacto.telefono")
    @Mapping(target = "correo", source = "contacto.correo")
    @Mapping(target = "direccion", source = "contacto.direccion")
    @Mapping(target = "usuarioCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "usuarioModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Sucursal sucursal, @MappingTarget SucursalJpaEntity entity);

   
    @Named("integerToEstado")
    public static Estado integerToEstado(Integer codigo) {
        return codigo == null ? null : Estado.fromCodigo(codigo);
    }

    @Named("estadoToCodigo")
    public static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
