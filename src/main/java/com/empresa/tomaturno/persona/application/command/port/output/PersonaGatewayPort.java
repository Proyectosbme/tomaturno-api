package com.empresa.tomaturno.persona.application.command.port.output;

import com.empresa.tomaturno.persona.dominio.entity.Persona;

/**
 * Lectura que el lado command necesita (resolver si ya existe una persona con ese DUI para
 * decidir crear vs. actualizar) sin depender del PersonaQueryRepository completo, que
 * pertenece al lado query.
 */
public interface PersonaGatewayPort {
    Persona buscarPorDui(String dui);
}
