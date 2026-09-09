package com.empresa.tomaturno.estadooperador.application.command.port.input;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public interface EstadoOperadorCommandInputPort {
    EstadoOperador abrirOperador(Long idUsuario, Long idSucursal, Long idPuesto);
    EstadoOperador cerrarOperador(Long idUsuario, Long idSucursal, Long idPuesto);
    EstadoOperador iniciarDescanso(Long idUsuario, Long idSucursal, Long idPuesto, Long idTipoDescanso, String comentario);
    EstadoOperador quitarDescanso(Long idUsuario, Long idSucursal, Long idPuesto);
}
