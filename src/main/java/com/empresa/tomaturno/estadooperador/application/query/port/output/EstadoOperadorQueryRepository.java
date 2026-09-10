package com.empresa.tomaturno.estadooperador.application.query.port.output;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public interface EstadoOperadorQueryRepository {
    EstadoOperador buscarVigente(Long idUsuario, Long idSucursal);
}
