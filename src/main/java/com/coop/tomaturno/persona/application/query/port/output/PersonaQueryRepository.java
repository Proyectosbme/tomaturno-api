package com.coop.tomaturno.persona.application.query.port.output;

import com.coop.tomaturno.persona.dominio.entity.Persona;

public interface PersonaQueryRepository {
    Persona buscarPorDui(String dui);
}
