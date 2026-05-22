package com.empresa.tomaturno.framework.adapters.output.mapper;

import java.util.List;

import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoJpaEntity;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatalogoOutputMapper {

    public Catalogo toDomain(CatalogoJpaEntity entity, List<CatalogoDetalle> detalles) {
        Auditoria auditoria = Auditoria.reconstituir(
                entity.getUsuarioCreacion(), entity.getFechaCreacion(),
                entity.getUsuarioModificacion(), entity.getFechaModificacion());
        return Catalogo.reconstruirCatalogo(
                entity.getId().intValue(),
                entity.getNombre(),
                entity.getDescripcion(),
                Estado.fromCodigo(entity.getEstado()),
                detalles,
                auditoria);
    }

    public CatalogoDetalle toDetalleDomain(CatalogoDetalleJpaEntity entity) {
        Auditoria auditoria = Auditoria.reconstituir(
                entity.getUsuarioCreacion(), entity.getFechaCreacion(),
                entity.getUsuarioModificacion(), entity.getFechaModificacion());
        return CatalogoDetalle.reconstruirCatalogoDetalle(
                entity.getNombre(),
                entity.getDescripcion(),
                Estado.fromCodigo(entity.getEstado()),
                auditoria);
    }

    public CatalogoDetalleJpaEntity toJpaEntity(long idCatalogo, long correlativo, CatalogoDetalle detalle) {
        CatalogoDetalleJpaEntity entity = new CatalogoDetalleJpaEntity();
        entity.setNombre(detalle.getNombre());
        entity.setDescripcion(detalle.getDescripcion());
        entity.setUsuarioCreacion(detalle.getAuditoria().getUsuarioCreacion());
        entity.setFechaCreacion(detalle.getAuditoria().getFechaCreacion());
        entity.setEstado(Estado.ACTIVO.getCodigo());
        return entity;
    }
}
