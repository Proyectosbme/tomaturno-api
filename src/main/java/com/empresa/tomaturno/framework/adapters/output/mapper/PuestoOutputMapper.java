package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.PuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;
import com.empresa.tomaturno.puesto.dominio.vo.Sucursal;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

@Mapper(componentModel = "cdi")
public interface PuestoOutputMapper {

    // ─── Dominio → JPA ────────────────────────────────────────────────────

    @Mapping(target = "idpk.id", source = "identificador")
    @Mapping(target = "idpk.idSucursal", source = "sucursal.identificador")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    PuestoJpaEntity toJpaEntity(Puesto puesto);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
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
        Auditoria auditoria = Auditoria.reconstituir(
                e.getUserCreacion(), e.getFechaCreacion(),
                e.getUserModificacion(), e.getFechaModificacion());
        return Puesto.reconstituir(
                e.getIdpk().getId(),
                e.getNombre(),
                e.getNombreLlamada(),
                Estado.fromCodigo(e.getEstado()),
                sucursalVo,
                auditoria);
    }

    @Named("estadoToCodigo")
    static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
