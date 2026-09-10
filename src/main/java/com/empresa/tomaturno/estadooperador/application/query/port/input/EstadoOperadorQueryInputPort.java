package com.empresa.tomaturno.estadooperador.application.query.port.input;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public interface EstadoOperadorQueryInputPort {
    EstadoOperador buscarVigente(Long idUsuario, Long idSucursal);
}
