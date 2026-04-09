package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

@Mapper(componentModel = "cdi")
public interface ConfiguracionOutputMapper {

    // ─── Dominio → JPA ────────────────────────────────────────────────────

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

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "idConfiguracion", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Configuracion configuracion, @MappingTarget ConfiguracionJpaEntity entity);

    // ─── JPA → Dominio ────────────────────────────────────────────────────

    default Configuracion toDomain(ConfiguracionJpaEntity e) {
        return toDomainConSucursal(e, null);
    }

    default Configuracion toDomainConSucursal(ConfiguracionJpaEntity e, SucursalJpaEntity sucursal) {
        Auditoria auditoria = Auditoria.reconstituir(
                e.getUserCreacion(), e.getFechaCreacion(),
                e.getUserModificacion(), e.getFechaModificacion());
        return Configuracion.builder()
                .idConfiguracion(e.getIdpk().getIdConfiguracion())
                .idSucursal(e.getIdpk().getIdSucursal())
                .nombre(e.getNombre())
                .parametro(e.getParametro())
                .valorTexto(e.getValorTexto())
                .descripcion(e.getDescripcion())
                .estado(Estado.fromCodigo(e.getEstado()))
                .auditoria(auditoria)
                .nombreSucursal(sucursal != null ? sucursal.getNombre() : null)
                .reconstituir();
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
