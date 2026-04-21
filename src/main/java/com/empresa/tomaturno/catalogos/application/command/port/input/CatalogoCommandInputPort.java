package com.empresa.tomaturno.catalogos.application.command.port.input;

import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;

public interface CatalogoCommandInputPort {
    Catalogo crear(Catalogo catalogo,String usuario);
    Catalogo crearDetalle(Long idCatalogo,CatalogoDetalle detalle, String usuario);
}
