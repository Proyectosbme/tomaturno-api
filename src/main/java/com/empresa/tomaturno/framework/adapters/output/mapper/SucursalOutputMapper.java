package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.shared.*;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;


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

    default Sucursal toDomain(SucursalJpaEntity e) {
        Contacto contacto = Contacto.reconstituir(e.getTelefono(), e.getCorreo(), e.getDireccion());
        Auditoria auditoria = Auditoria.reconstituir(
                e.getUsuarioCreacion(), e.getFechaCreacion(),
                e.getUsuarioModificacion(), e.getFechaModificacion());
        return Sucursal.reconstituir(e.getId(), e.getNombre(), contacto, Estado.fromCodigo(e.getEstado()), auditoria);
    }

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

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
