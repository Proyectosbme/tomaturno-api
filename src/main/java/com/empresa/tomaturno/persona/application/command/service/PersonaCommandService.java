package com.coop.tomaturno.persona.application.command.service;

import com.coop.tomaturno.persona.application.command.port.input.PersonaCommandInputPort;
import com.coop.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.coop.tomaturno.persona.application.command.usecase.CrearOActualizarPersonaUseCase;
import com.coop.tomaturno.persona.application.query.port.output.PersonaQueryRepository;
import com.coop.tomaturno.persona.dominio.entity.Persona;

public class PersonaCommandService implements PersonaCommandInputPort {

    private final CrearOActualizarPersonaUseCase crearOActualizarPersonaUseCase;

    public PersonaCommandService(PersonaCommandRepository personaCommandRepository,
            PersonaQueryRepository personaQueryRepository) {
        this.crearOActualizarPersonaUseCase = new CrearOActualizarPersonaUseCase(
                personaCommandRepository, personaQueryRepository);
    }

    @Override
    public Persona crearOActualizar(Persona persona) {
        return crearOActualizarPersonaUseCase.ejecutar(persona);
    }
}
