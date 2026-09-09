package com.empresa.tomaturno.estadooperador.application.command.port.output;

import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public interface EstadoOperadorCommandRepository {
    EstadoOperador guardar(EstadoOperador estadoOperador);
    EstadoOperador actualizar(EstadoOperador estadoOperador);
}
