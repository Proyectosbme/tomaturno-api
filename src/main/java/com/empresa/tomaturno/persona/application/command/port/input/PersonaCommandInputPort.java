package com.empresa.tomaturno.persona.application.command.port.input;

import com.empresa.tomaturno.persona.dominio.entity.Persona;

public interface PersonaCommandInputPort {
    Persona crearOActualizar(Persona persona);
}
