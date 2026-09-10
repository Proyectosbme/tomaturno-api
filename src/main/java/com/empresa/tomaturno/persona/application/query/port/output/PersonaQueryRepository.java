package com.empresa.tomaturno.persona.application.query.port.output;

import com.empresa.tomaturno.persona.dominio.entity.Persona;

public interface PersonaQueryRepository {
    Persona buscarPorDui(String dui);
}
