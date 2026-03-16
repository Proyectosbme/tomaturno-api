package com.empresa.tomaturno.persona.application.command.port.output;

import com.empresa.tomaturno.persona.dominio.entity.Persona;

public interface PersonaCommandRepository {
    Persona save(Persona persona);
    Persona update(Persona persona);
}
