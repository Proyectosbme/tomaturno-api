package com.empresa.tomaturno.turno.application.query.port.input;

import java.util.List;

import com.empresa.tomaturno.turno.application.query.dto.TurnoHoyDTO;

public interface TurnoHoyQueryInputPort {
    List<TurnoHoyDTO> buscarTodos(Long idSucursal);
    List<TurnoHoyDTO> buscarPorUsuario(Long idUsuario, Long idSucursal);
}
