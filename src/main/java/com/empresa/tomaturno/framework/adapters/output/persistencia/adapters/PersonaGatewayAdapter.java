package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.persona.application.command.port.output.PersonaGatewayPort;
import com.empresa.tomaturno.persona.application.query.port.output.PersonaQueryRepository;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

public class PersonaGatewayAdapter implements PersonaGatewayPort {

    private final PersonaQueryRepository personaQueryRepository;

    public PersonaGatewayAdapter(PersonaQueryRepository personaQueryRepository) {
        this.personaQueryRepository = personaQueryRepository;
    }

    @Override
    public Persona buscarPorDui(String dui) {
        return personaQueryRepository.buscarPorDui(dui);
    }
}
