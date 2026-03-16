package com.empresa.tomaturno.turno.application.command.port.output;

import com.empresa.tomaturno.turno.dominio.entity.Turno;

public interface TurnoCommandRepository {
    Turno save(Turno turno);
    Turno actualizar(Turno turno);
    /** Marca el original como TRASLADO y persiste el nuevo turno */
    Turno reasignar(Turno turnoOriginal, Turno turnoNuevo);
}
