package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

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

    default Usuario toDomain(UsuarioJpaEntity e) {
        return Usuario.builder()
                .identificador(e.getIdpk().getId())
                .idSucursal(e.getIdpk().getIdSucursal())
                .idPuesto(e.getIdPuesto())
                .codigoUsuario(e.getCodigoUsuario())
                .contrasena(e.getContrasena())
                .estado(Estado.fromCodigo(e.getEstado()))
                .datosPersonales(DatosPersonales.reconstituir(
                        e.getNombres(), e.getApellidos(), e.getDui(), e.getTelefono()))
                .configuracion(ConfiguracionOperador.reconstituir(
                        e.getPerfil(), e.getIp(), e.getCorrelativo(), e.getAtenderCasosEspeciales()))
                .auditoria(Auditoria.reconstituir(
                        e.getUserCreacion(), e.getFechaCreacion(),
                        e.getUserModificacion(), e.getFechaModificacion()))
                .build();
    }

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Usuario usuario, @MappingTarget UsuarioJpaEntity entity);

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
