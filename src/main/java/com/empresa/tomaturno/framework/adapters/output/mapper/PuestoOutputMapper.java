package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.vo.Auditoria;
import com.empresa.tomaturno.puesto.dominio.vo.Estado;
import com.empresa.tomaturno.puesto.dominio.vo.Sucursal;

@Mapper(componentModel = "cdi")
public interface PuestoOutputMapper {

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
    PuestoJpaEntity toJpaEntity(Puesto puesto);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoriaCreacion.usuario")
    @Mapping(target = "fechaCreacion", source = "auditoriaCreacion.fecha")
    @Mapping(target = "userModificacion", source = "auditoriaModificacion.usuario")
    @Mapping(target = "fechaModificacion", source = "auditoriaModificacion.fecha")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Puesto puesto, @MappingTarget PuestoJpaEntity entity);

    // ─── JPA → Dominio ────────────────────────────────────────────────────

    default Puesto toDomain(PuestoJpaEntity e) {
        return toDomainConSucursal(e, null);
    }

    default Puesto toDomainConSucursal(PuestoJpaEntity e, SucursalJpaEntity sucursal) {
        Sucursal sucursalVo = sucursal != null
                ? new Sucursal(sucursal.getId(), sucursal.getNombre())
                : new Sucursal(e.getIdpk().getIdSucursal(), null);
        Auditoria auditoriaCreacion = Auditoria.reconstituir(e.getUserCreacion(), e.getFechaCreacion());
        Auditoria auditoriaModificacion = Auditoria.reconstituir(e.getUserModificacion(), e.getFechaModificacion());
        return Puesto.of(new Puesto.Builder()
                .identificador(e.getIdpk().getId())
                .nombre(e.getNombre())
                .nombreLlamada(e.getNombreLlamada())
                .estado(Estado.fromCodigo(e.getEstado()))
                .sucursal(sucursalVo)
                .auditoriaCreacion(auditoriaCreacion)
                .auditoriaModificacion(auditoriaModificacion));
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
