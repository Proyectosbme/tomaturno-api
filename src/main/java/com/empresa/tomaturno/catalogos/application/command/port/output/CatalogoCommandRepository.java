package com.empresa.tomaturno.catalogos.application.command.port.output;

import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;

public interface CatalogoCommandRepository {
    Catalogo crear(Catalogo catalogo);
    Catalogo crearDetalle(long idCatalogo, CatalogoDetalle detalle);
}
