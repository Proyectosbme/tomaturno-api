package com.empresa.tomaturno.catalogos.application.command.usecase;

import com.empresa.tomaturno.catalogos.application.command.port.output.CatalogoCommandRepository;
import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;

public class CrearCatalogoDetalleUseCase {

    private final CatalogoCommandRepository catalogoCommandRepository;

    public CrearCatalogoDetalleUseCase(CatalogoCommandRepository catalogoCommandRepository) {
        this.catalogoCommandRepository = catalogoCommandRepository;
    }

    public Catalogo ejecutar(Long idCatalogo, CatalogoDetalle detalle, String usuario) {
        detalle.crear(usuario);
        return catalogoCommandRepository.crearDetalle(idCatalogo, detalle);
    }
}
