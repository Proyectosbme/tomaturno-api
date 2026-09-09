package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.vo.Auditoria;
import com.empresa.tomaturno.configuracion.dominio.vo.Estado;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;

@Mapper(componentModel = "cdi")
public interface ConfiguracionOutputMapper {

    // ─── Dominio → JPA ────────────────────────────────────────────────────

    @Mapping(target = "idpk.idConfiguracion", source = "idConfiguracion")
    @Mapping(target = "idpk.idSucursal", source = "idSucursal")
    @Mapping(target = "idConfiguracion", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    ConfiguracionJpaEntity toJpaEntity(Configuracion configuracion);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "idConfiguracion", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Configuracion configuracion, @MappingTarget ConfiguracionJpaEntity entity);

    // ─── JPA → Dominio ────────────────────────────────────────────────────

    default Configuracion toDomain(ConfiguracionJpaEntity e) {
        return toDomainConSucursal(e, null);
    }

    default Configuracion toDomainConSucursal(ConfiguracionJpaEntity e, SucursalJpaEntity sucursal) {
        Auditoria auditoriaCreacion = Auditoria.reconstituir(e.getUserCreacion(), e.getFechaCreacion());
        Auditoria auditoriaModificacion = Auditoria.reconstituir(e.getUserModificacion(), e.getFechaModificacion());
        return Configuracion.of(new Configuracion.Builder()
                .idConfiguracion(e.getIdpk().getIdConfiguracion())
                .idSucursal(e.getIdpk().getIdSucursal())
                .nombre(e.getNombre())
                .parametro(e.getParametro())
                .descripcion(e.getDescripcion())
                .estado(Estado.fromCodigo(e.getEstado()))
                .auditoriaCreacion(auditoriaCreacion)
                .auditoriaModificacion(auditoriaModificacion)
                .nombreSucursal(sucursal != null ? sucursal.getNombre() : null));
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
