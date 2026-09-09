package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.Auditoria;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

@Mapper(componentModel = "cdi")
public interface UsuarioOutputMapper {

    @Mapping(target = "idpk.id", source = "identificador")
    @Mapping(target = "idpk.idSucursal", source = "idSucursal")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "foto", source = "foto")
    @Mapping(target = "nombreCompleto", source = ".", qualifiedByName = "nombreCompleto")
    @Mapping(target = "correlativoPuesto", source = "configuracion.correlativoPuesto")
    @Mapping(target = "ip", source = "configuracion.ip")
    @Mapping(target = "atenderCasosEspeciales", source = "configuracion.atenderCasosEspeciales")
    UsuarioJpaEntity toJpaEntity(Usuario usuario);

    default Usuario toDomain(UsuarioJpaEntity e) {
        Auditoria auditoriaCreacion = Auditoria.reconstituir(e.getUserCreacion(), e.getFechaCreacion());
        Auditoria auditoriaModificacion = Auditoria.reconstituir(e.getUserModificacion(), e.getFechaModificacion());
        return Usuario.of(new Usuario.Builder()
                .identificador(e.getIdpk().getId())
                .idSucursal(e.getIdpk().getIdSucursal())
                .idPuesto(e.getIdPuesto())
                .codigoUsuario(e.getCodigoUsuario())
                .keycloakId(e.getKeycloakId())
                .estado(Estado.fromCodigo(e.getEstado()))
                .datosPersonales(DatosPersonales.reconstituir(null, null, e.getDui(), e.getTelefono()))
                .configuracion(ConfiguracionOperador.reconstituir(
                        null, e.getIp(), e.getCorrelativoPuesto(), e.getAtenderCasosEspeciales()))
                .auditoriaCreacion(auditoriaCreacion)
                .auditoriaModificacion(auditoriaModificacion)
                .foto(e.getFoto()));
    }

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "foto", source = "foto")
     @Mapping(target = "nombreCompleto", source = ".", qualifiedByName = "nombreCompleto")
    void updateEntityFromDomain(Usuario usuario, @MappingTarget UsuarioJpaEntity entity);

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }

    @Named("nombreCompleto")
    static String nombreCompleto(Usuario usuario){
        return usuario.getNombres() + " " + usuario.getApellidos();
    }
}
