package com.coop.tomaturno.persona.application.command.port.output;

import com.coop.tomaturno.persona.dominio.entity.Persona;

public interface PersonaCommandRepository {
    Persona save(Persona persona);
    Persona update(Persona persona);
}
