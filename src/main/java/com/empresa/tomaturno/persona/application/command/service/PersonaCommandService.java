package com.empresa.tomaturno.persona.application.command.service;

import com.empresa.tomaturno.persona.application.command.port.input.PersonaCommandInputPort;
import com.empresa.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.empresa.tomaturno.persona.application.command.port.output.PersonaGatewayPort;
import com.empresa.tomaturno.persona.application.command.usecase.CrearOActualizarPersonaUseCase;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

public class PersonaCommandService implements PersonaCommandInputPort {

    private final CrearOActualizarPersonaUseCase crearOActualizarPersonaUseCase;

    public PersonaCommandService(PersonaCommandRepository personaCommandRepository,
            PersonaGatewayPort personaGatewayPort) {
        this.crearOActualizarPersonaUseCase = new CrearOActualizarPersonaUseCase(
                personaCommandRepository, personaGatewayPort);
    }

    @Override
    public Persona crearOActualizar(Persona persona) {
        return crearOActualizarPersonaUseCase.ejecutar(persona);
    }
}
