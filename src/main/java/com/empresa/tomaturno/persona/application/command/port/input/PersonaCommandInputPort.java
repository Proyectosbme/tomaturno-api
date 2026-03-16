package com.coop.tomaturno.persona.application.command.port.input;

import com.coop.tomaturno.persona.dominio.entity.Persona;

public interface PersonaCommandInputPort {
    Persona crearOActualizar(Persona persona);
}
