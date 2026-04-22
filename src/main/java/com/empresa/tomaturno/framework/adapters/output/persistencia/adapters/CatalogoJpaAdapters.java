package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;

import com.empresa.tomaturno.catalogos.application.command.port.output.CatalogoCommandRepository;
import com.empresa.tomaturno.catalogos.application.query.port.output.CatalogoQueryRepository;
import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntityPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.CatalogoDetalleJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.CatalogoJpaRepository;
import com.empresa.tomaturno.shared.clases.Auditoria;
import com.empresa.tomaturno.shared.clases.Estado;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CatalogoJpaAdapters implements CatalogoCommandRepository, CatalogoQueryRepository {

    private final CatalogoJpaRepository catalogoJpaRepository;
    private final CatalogoDetalleJpaRepository catalogoDetalleJpaRepository;

    @Inject
    public CatalogoJpaAdapters(CatalogoJpaRepository catalogoJpaRepository,
                               CatalogoDetalleJpaRepository catalogoDetalleJpaRepository) {
        this.catalogoJpaRepository = catalogoJpaRepository;
        this.catalogoDetalleJpaRepository = catalogoDetalleJpaRepository;
    }

    @Override
    public Catalogo obtenerCatalogoConDetallesPorId(Long idCatalogo) {
        CatalogoJpaEntity entity = catalogoJpaRepository.findById(idCatalogo);
        if (entity == null) return null;

        List<CatalogoDetalleJpaEntity> detallesEntity = catalogoDetalleJpaRepository.buscarPorIdCatalogo(idCatalogo);

        List<CatalogoDetalle> detalles = detallesEntity.stream()
                .map(this::toDetalleDomain)
                .toList();

        return toCatalogoDomain(entity, detalles);
    }

    @Override
    public Long obterCorrelativoCatalogo() {
        return catalogoJpaRepository.obtenerSiguienteCorrelativo();
    }

    @Override
    public Long obtenerCorrelativoDetalleCatalogo(Long idCatalogo) {
        return catalogoDetalleJpaRepository.obtenerSiguienteCorrelativo(idCatalogo);
    }

    @Override
    public Catalogo crear(Catalogo catalogo) {
        CatalogoJpaEntity entity = new CatalogoJpaEntity();
        entity.setNombre(catalogo.getNombre());
        entity.setDescripcion(catalogo.getDescripcion());
        entity.setUsuarioCreacion(catalogo.getAuditoria().getUsuarioCreacion());
        entity.setFechaCreacion(catalogo.getAuditoria().getFechaCreacion());
        entity.setEstado(Estado.ACTIVO.getCodigo());
        catalogoJpaRepository.persist(entity);
        catalogoJpaRepository.flush();

        return obtenerCatalogoConDetallesPorId(entity.getId());
    }

    @Override
    public Catalogo crearDetalle(long idCatalogo, CatalogoDetalle detalle) {
        CatalogoDetalleJpaEntity entity = new CatalogoDetalleJpaEntity();
        entity.setId(new CatalogoDetalleJpaEntityPK(idCatalogo, detalle.getCorrelativo().longValue()));
        entity.setNombre(detalle.getNombre());
        entity.setDescripcion(detalle.getDescripcion());
        entity.setUsuarioCreacion(detalle.getAuditoria().getUsuarioCreacion());
        entity.setFechaCreacion(detalle.getAuditoria().getFechaCreacion());
        entity.setEstado(Estado.ACTIVO.getCodigo());
        catalogoDetalleJpaRepository.persist(entity);

        return obtenerCatalogoConDetallesPorId(idCatalogo);
    }

    private Catalogo toCatalogoDomain(CatalogoJpaEntity entity, List<CatalogoDetalle> detalles) {
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

    private CatalogoDetalle toDetalleDomain(CatalogoDetalleJpaEntity entity) {
        Auditoria auditoria = Auditoria.reconstituir(
                entity.getUsuarioCreacion(), entity.getFechaCreacion(),
                entity.getUsuarioModificacion(), entity.getFechaModificacion());
        CatalogoDetalle detalle = CatalogoDetalle.reconstruirCatalogoDetalle(
                entity.getNombre(),
                entity.getDescripcion(),
                Estado.fromCodigo(entity.getEstado()),
                auditoria);
        detalle.asignarCorrelativo(entity.getId().getIdDetalle().intValue());
        return detalle;
    }
}
