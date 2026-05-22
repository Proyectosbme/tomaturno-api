package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import java.util.List;

import com.empresa.tomaturno.catalogos.application.command.port.output.CatalogoCommandRepository;
import com.empresa.tomaturno.catalogos.application.query.port.output.CatalogoQueryRepository;
import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;
import com.empresa.tomaturno.framework.adapters.output.mapper.CatalogoOutputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntityPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.CatalogoDetalleJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.CatalogoJpaRepository;
import com.empresa.tomaturno.shared.clases.Estado;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CatalogoJpaAdapters implements CatalogoCommandRepository, CatalogoQueryRepository {

    private final CatalogoJpaRepository catalogoJpaRepository;
    private final CatalogoDetalleJpaRepository catalogoDetalleJpaRepository;
    private final CatalogoOutputMapper mapper;

    @Inject
    public CatalogoJpaAdapters(CatalogoJpaRepository catalogoJpaRepository,
                               CatalogoDetalleJpaRepository catalogoDetalleJpaRepository,
                               CatalogoOutputMapper mapper) {
        this.catalogoJpaRepository = catalogoJpaRepository;
        this.catalogoDetalleJpaRepository = catalogoDetalleJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Catalogo obtenerCatalogoConDetallesPorId(Long idCatalogo) {
        CatalogoJpaEntity entity = catalogoJpaRepository.findById(idCatalogo);
        if (entity == null) return null;

        List<CatalogoDetalle> detalles = catalogoDetalleJpaRepository.buscarPorIdCatalogo(idCatalogo)
                .stream().map(mapper::toDetalleDomain).toList();

        return mapper.toDomain(entity, detalles);
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
    public Catalogo crearDetalle(
        long idCatalogo, 
        CatalogoDetalle detalle) {
        Long correlativo = catalogoDetalleJpaRepository.obtenerSiguienteCorrelativo(idCatalogo);
        CatalogoDetalleJpaEntity entity = mapper.toJpaEntity(idCatalogo, correlativo, detalle);
        entity.setId(new CatalogoDetalleJpaEntityPK(idCatalogo, correlativo));
        catalogoDetalleJpaRepository.persist(entity);

        return obtenerCatalogoConDetallesPorId(idCatalogo);
    }
}
