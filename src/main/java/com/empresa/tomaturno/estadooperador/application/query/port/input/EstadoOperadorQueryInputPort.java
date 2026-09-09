package com.empresa.tomaturno.estadooperador.application.query.port.input;

import java.time.LocalDate;
import java.util.List;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public interface EstadoOperadorQueryInputPort {
    EstadoOperador buscarVigente(Long idUsuario, Long idSucursal);
    List<EstadoOperador> buscarHistorial(Long idUsuario, Long idSucursal, LocalDate desde, LocalDate hasta);
}
