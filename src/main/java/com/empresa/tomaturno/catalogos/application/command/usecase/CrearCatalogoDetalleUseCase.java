package com.empresa.tomaturno.catalogos.application.command.usecase;

import com.empresa.tomaturno.catalogos.application.command.port.output.CatalogoCommandRepository;
import com.empresa.tomaturno.catalogos.application.query.port.output.CatalogoQueryRepository;
import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;

public class CrearCatalogoDetalleUseCase {

    private final CatalogoCommandRepository catalogoCommandRepository;
      private final CatalogoQueryRepository catalogoQueryRepository;

    public CrearCatalogoDetalleUseCase(CatalogoCommandRepository catalogoCommandRepository, CatalogoQueryRepository catalogoQueryRepository) {
        this.catalogoCommandRepository = catalogoCommandRepository;
        this.catalogoQueryRepository = catalogoQueryRepository;
    }

    public Catalogo ejecutar(Long idCatalogo, CatalogoDetalle detalle, String usuario) {
        detalle.asignarCorrelativo(catalogoQueryRepository.obtenerCorrelativoDetalleCatalogo(idCatalogo).intValue());
        detalle.crear(usuario);
        return catalogoCommandRepository.crearDetalle(idCatalogo, detalle);
    }
}
