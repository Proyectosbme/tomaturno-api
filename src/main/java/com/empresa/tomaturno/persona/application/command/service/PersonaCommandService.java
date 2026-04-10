package com.empresa.tomaturno.persona.application.command.service;

import com.empresa.tomaturno.persona.application.command.port.input.PersonaCommandInputPort;
import com.empresa.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.empresa.tomaturno.persona.application.command.usecase.AsignarFotoUseCase;
import com.empresa.tomaturno.persona.application.command.usecase.CrearOActualizarPersonaUseCase;
import com.empresa.tomaturno.persona.application.query.port.output.PersonaQueryRepository;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

public class PersonaCommandService implements PersonaCommandInputPort {

    private final CrearOActualizarPersonaUseCase crearOActualizarPersonaUseCase;
    private final AsignarFotoUseCase asignarFotoUseCase;

    public PersonaCommandService(PersonaCommandRepository personaCommandRepository,
            PersonaQueryRepository personaQueryRepository) {
        this.crearOActualizarPersonaUseCase = new CrearOActualizarPersonaUseCase(
                personaCommandRepository, personaQueryRepository);
        this.asignarFotoUseCase = new AsignarFotoUseCase(
                personaCommandRepository, personaQueryRepository);
    }

    @Override
    public Persona crearOActualizar(Persona persona) {
        return crearOActualizarPersonaUseCase.ejecutar(persona);
    }

    @Override
    public Persona asignarFoto(Long id, byte[] foto) {
        return asignarFotoUseCase.ejecutar(id, foto);
    }
}
