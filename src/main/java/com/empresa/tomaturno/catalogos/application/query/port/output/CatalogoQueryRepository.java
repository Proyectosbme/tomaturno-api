package com.empresa.tomaturno.catalogos.application.query.port.output;

import com.empresa.tomaturno.catalogos.dominio.entity.Catalogo;

public interface CatalogoQueryRepository {

    Catalogo obtenerCatalogoConDetallesPorId(Long idCatalogo) ;
    Long obterCorrelativoCatalogo();
    Long obtenerCorrelativoDetalleCatalogo(Long idCatalogo);
    
}
