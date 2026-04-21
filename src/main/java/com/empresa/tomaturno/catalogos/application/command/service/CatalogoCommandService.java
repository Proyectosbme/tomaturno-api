package com.empresa.tomaturno.catalogos.application.command.service;

import com.empresa.tomaturno.catalogos.application.command.port.input.CatalogoCommandInputPort;
import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;
import com.empresa.tomaturno.catalogos.dominio.entity.CatalogoDetalle;

public class CatalogoCommandService  implements CatalogoCommandInputPort {

    @Override
    public Catalogo crear(Catalogo catalogo, String usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crear'");
    }

    @Override
    public Catalogo crearDetalle(Long idCatalogo, CatalogoDetalle detalle, String usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearDetalle'");
    }
    
   
    
}
