package com.empresa.tomaturno.framework.adapters.output.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;
import com.empresa.tomaturno.cola.dominio.vo.Sucursal;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;

@Mapper(componentModel = "cdi")
public interface ColaOutputMapper {

    // ─── Dominio → JPA ────────────────────────────────────────────────────

    @Mapping(target = "idpk.id", source = "identificador")
    @Mapping(target = "idpk.idSucursal", source = "sucursal.identificador")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    ColaJpaEntity toColaJpaEntity(Cola cola);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Cola cola, @MappingTarget ColaJpaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "codigo", source = "codigo")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "userCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    void updateDetalleEntityFromDomain(Detalle detalle, @MappingTarget DetalleColaJpaEntity entity);

    @Mapping(target = "id.idCola", source = "idCola")
    @Mapping(target = "id.idSucursal", source = "idSucursal")
    @Mapping(target = "id.idDetalle", source = "detalle.correlativo")
    @Mapping(target = "nombre", source = "detalle.nombre")
    @Mapping(target = "codigo", source = "detalle.codigo")
    @Mapping(target = "estado", source = "detalle.estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "userCreacion", source = "detalle.auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "detalle.auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "detalle.auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "detalle.auditoriaModificacion.fecha")
    DetalleColaJpaEntity toDetalleJpaEntity(Long idCola, Integer idSucursal, Detalle detalle);

    // ─── JPA → Dominio ────────────────────────────────────────────────────

    /** Cola básica sin nombre de sucursal ni detalles. */
    default Cola toDomain(ColaJpaEntity e) {
        return toDomainCompleto(e, null, null);
    }

    /** Cola con nombre de sucursal cargado, sin detalles. */
    default Cola toDomainConSucursal(ColaJpaEntity e, SucursalJpaEntity sucursal) {
        return toDomainCompleto(e, sucursal, null);
    }

    /** Cola con detalles cargados, sin nombre de sucursal. */
    default Cola toDomainConDetalles(ColaJpaEntity e, List<DetalleColaJpaEntity> detalles) {
        return toDomainCompleto(e, null, detalles);
    }

    /** Cola completa: nombre de sucursal + detalles. */
    default Cola toDomainCompleto(ColaJpaEntity e, SucursalJpaEntity sucursal,
            List<DetalleColaJpaEntity> detalles) {

        Sucursal sucursalVo = sucursal != null
                ? new Sucursal(sucursal.getId(), sucursal.getNombre())
                : new Sucursal(e.getIdpk().getIdSucursal(), null);

        Auditoria auditoriaCreacion = Auditoria.reconstituir(e.getUserCreacion(), e.getFechaCreacion());
        Auditoria auditoriaModificacion = Auditoria.reconstituir(e.getUserModificacion(), e.getFechaModificacion());

        List<Detalle> detallesDomain = detalles != null
                ? detalles.stream().map(this::toDomainDetalle).toList()
                : null;

        return Cola.of(new Cola.Builder()
                .identificador(e.getIdpk().getId())
                .nombre(e.getNombre())
                .codigo(e.getCodigo())
                .estado(Estado.fromCodigo(e.getEstado()))
                .sucursal(sucursalVo)
                .auditoriaCreacion(auditoriaCreacion)
                .auditoriaModificacion(auditoriaModificacion)
                .detalles(detallesDomain));
    }

    /** Reconstitución de un Detalle ya persistido: no recompone código ni valida unicidad. */
    default Detalle toDomainDetalle(DetalleColaJpaEntity e) {
        Auditoria auditoriaCreacion = Auditoria.reconstituir(e.getUserCreacion(), e.getFechaCreacion());
        Auditoria auditoriaModificacion = Auditoria.reconstituir(e.getUserModificacion(), e.getFechaModificacion());
        return Cola.reconstituirDetalle(new Detalle.Builder()
                .correlativo(e.getId().getIdDetalle())
                .nombre(e.getNombre())
                .codigo(e.getCodigo())
                .estado(Estado.fromCodigo(e.getEstado()))
                .auditoriaCreacion(auditoriaCreacion)
                .auditoriaModificacion(auditoriaModificacion));
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
