package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;


@Mapper(componentModel = "cdi")
public interface SucursalOutputMapper {

    @Mapping(target = "id", source = "identificador")
    @Mapping(target = "telefono", source = "contacto.telefono")
    @Mapping(target = "correo", source = "contacto.correo")
    @Mapping(target = "direccion", source = "contacto.direccion")
    @Mapping(target = "usuarioCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "usuarioModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    SucursalJpaEntity toSucursalJpaEntity(Sucursal sucursal);

    default Sucursal toDomain(SucursalJpaEntity e) {
        Contacto contacto = Contacto.reconstituir(e.getTelefono(), e.getCorreo(), e.getDireccion());
        Auditoria auditoriaCreacion = Auditoria.reconstituir(e.getUsuarioCreacion(), e.getFechaCreacion());
        Auditoria auditoriaModificacion = Auditoria.reconstituir(e.getUsuarioModificacion(), e.getFechaModificacion());
        return Sucursal.of(new Sucursal.Builder()
                .identificador(e.getId())
                .nombre(e.getNombre())
                .contacto(contacto)
                .estado(Estado.fromCodigo(e.getEstado()))
                .auditoriaCreacion(auditoriaCreacion)
                .auditoriaModificacion(auditoriaModificacion));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "telefono", source = "contacto.telefono")
    @Mapping(target = "correo", source = "contacto.correo")
    @Mapping(target = "direccion", source = "contacto.direccion")
    @Mapping(target = "usuarioCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "usuarioModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Sucursal sucursal, @MappingTarget SucursalJpaEntity entity);

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
