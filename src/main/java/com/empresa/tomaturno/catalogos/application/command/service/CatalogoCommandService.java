package com.empresa.tomaturno.catalogos.application.command.service;

import com.empresa.tomaturno.catalogos.application.command.port.input.CatalogoCommandInputPort;
import com.empresa.tomaturno.catalogos.application.command.usecase.CrearCatalogoDetalleUseCase;
import com.empresa.tomaturno.catalogos.application.command.usecase.CrearCatalogoUseCase;
import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;

public class CatalogoCommandService  implements CatalogoCommandInputPort {
    private final CrearCatalogoDetalleUseCase crearCatalogoDetalleUseCase;
    private final CrearCatalogoUseCase crearCatalogoUseCase;

    public CatalogoCommandService(CrearCatalogoDetalleUseCase crearCatalogoDetalleUseCase, 
        CrearCatalogoUseCase crearCatalogoUseCase) {
        this.crearCatalogoDetalleUseCase = crearCatalogoDetalleUseCase;
        this.crearCatalogoUseCase = crearCatalogoUseCase;
    }

    @Override
    public Catalogo crear(Catalogo catalogo, String usuario) {
        return crearCatalogoUseCase.ejecutar(catalogo, usuario);
    }

    @Override
    public Catalogo crearDetalle(Long idCatalogo, CatalogoDetalle detalle, String usuario) {
        return crearCatalogoDetalleUseCase.ejecutar(idCatalogo, detalle, usuario);
    }   
   
}
