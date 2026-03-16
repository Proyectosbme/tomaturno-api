package com.empresa.tomaturno.framework.adapters.output.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Estado;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ColaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaJpaEntity;

@Mapper(componentModel = "cdi")
public interface ColaOutputMapper {

    @Mapping(target = "idpk.id", source = "identificador")
    @Mapping(target = "idpk.idSucursal", source = "sucursal.identificador")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "prioridad", source = "prioridad")
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    ColaJpaEntity toColaJpaEntity(Cola cola);

    @Mapping(target = "identificador", source = "idpk.id")
    @Mapping(target = "sucursal.identificador", source = "idpk.idSucursal")
    @Mapping(target = "sucursal.nombre", ignore = true)
    @Mapping(target = "prioridad", source = "prioridad")
    @Mapping(target = "auditoria.usuarioCreacion", source = "userCreacion")
    @Mapping(target = "auditoria.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "auditoria.usuarioModificacion", source = "userModificacion")
    @Mapping(target = "auditoria.fechaModificacion", source = "fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    Cola toDomain(ColaJpaEntity colaJpaEntity);

    @Mapping(target = "idpk", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idSucursal", ignore = true)
    @Mapping(target = "prioridad", source = "prioridad")
    @Mapping(target = "userCreacion", source = "auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "auditoria.fechaModificacion")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoToCodigo")
    void updateEntityFromDomain(Cola cola, @MappingTarget ColaJpaEntity entity);

    @Mapping(target = "id.idCola", source = "idCola")
    @Mapping(target = "id.idSucursal", source = "idSucursal")
    @Mapping(target = "id.idDetalle", source = "detalle.correlativo")
    @Mapping(target = "nombre", source = "detalle.nombre")
    @Mapping(target = "codigo", source = "detalle.codigo")
    @Mapping(target = "estado", source = "detalle.estado", qualifiedByName = "estadoToCodigo")
    @Mapping(target = "userCreacion", source = "detalle.auditoria.usuarioCreacion")
    @Mapping(target = "fechaCreacion", source = "detalle.auditoria.fechaCreacion")
    @Mapping(target = "userModificacion", source = "detalle.auditoria.usuarioModificacion")
    @Mapping(target = "fechaModificacion", source = "detalle.auditoria.fechaModificacion")
    DetalleColaJpaEntity toDetalleJpaEntity(Long idCola, Integer idSucursal, Detalle detalle);

    @Mapping(target = "correlativo", source = "id.idDetalle")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "codigo", source = "codigo")
    @Mapping(target = "estado", source = "estado", qualifiedByName = "integerToEstado")
    @Mapping(target = "auditoria.usuarioCreacion", source = "userCreacion")
    @Mapping(target = "auditoria.fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "auditoria.usuarioModificacion", source = "userModificacion")
    @Mapping(target = "auditoria.fechaModificacion", source = "fechaModificacion")
    Detalle toDomainDetalle(DetalleColaJpaEntity entity);

    @Named("integerToEstado")
    public static Estado integerToEstado(Integer codigo) {
        return codigo == null ? null : Estado.fromCodigo(codigo);
    }

    @Named("estadoToCodigo")
    public static Integer estadoToCodigo(Estado estado) {
        return estado == null ? null : estado.getCodigo();
    }
}
