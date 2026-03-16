package com.coop.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.coop.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;
import com.coop.tomaturno.usuario.dominio.vo.Estado;

@Mapper(componentModel = "cdi")
public interface UsuarioOutputMapper {

    @Mapping(target = "idpk.id", source = "identificador")
    @Mapping(target = "idpk.idSucursal", source = "idSucursal")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    UsuarioJpaEntity toJpaEntity(Usuario usuario);

    @Mapping(target = "identificador", source = "idpk.id")
    @Mapping(target = "idSucursal", source = "idpk.idSucursal")
    @Mapping(target = "auditoria.usuarioCreacion", source = "userCreacion")
    @Mapping(target = "auditoria.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "auditoria.usuarioModificacion", source = "userModificacion")
    @Mapping(target = "auditoria.fechaModificacion", source = "fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    Usuario toDomain(UsuarioJpaEntity entity);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Usuario usuario, @MappingTarget UsuarioJpaEntity entity);

    @Named("integerToEstado")
    static Estado integerToEstado(Integer codigo) {
        return codigo == null ? null : Estado.fromCodigo(codigo);
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
