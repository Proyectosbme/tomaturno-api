package com.empresa.tomaturno.catalogos.application.command.usecase;

import com.empresa.tomaturno.catalogos.application.command.port.output.CatalogoCommandRepository;
import com.empresa.tomaturno.catalogos.application.query.port.output.CatalogoQueryRepository;
import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;

public class CrearCatalogoUseCase {
    private final CatalogoCommandRepository catalogoCommandRepository;
    private final CatalogoQueryRepository catalogoQueryRepository;

    public CrearCatalogoUseCase(CatalogoCommandRepository catalogoCommandRepository, CatalogoQueryRepository catalogoQueryRepository) {
        this.catalogoCommandRepository = catalogoCommandRepository;
        this.catalogoQueryRepository = catalogoQueryRepository;
    }

    public Catalogo ejecutar(Catalogo catalogo, String usuario) {
        catalogo.asignarIdentificador(catalogoQueryRepository.obterCorrelativoCatalogo().intValue() );
        catalogo.crear(usuario);
        return catalogoCommandRepository.crear(catalogo);
    }
}
