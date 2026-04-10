package com.empresa.tomaturno.persona.application.command.usecase;

import com.empresa.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.empresa.tomaturno.persona.application.query.port.output.PersonaQueryRepository;
import com.empresa.tomaturno.persona.dominio.entity.Persona;
import com.empresa.tomaturno.persona.dominio.exceptions.PersonaNotFoundException;

public class AsignarFotoUseCase {

    private final PersonaCommandRepository personaCommandRepository;
    private final PersonaQueryRepository personaQueryRepository;

    public AsignarFotoUseCase(PersonaCommandRepository personaCommandRepository,
            PersonaQueryRepository personaQueryRepository) {
        this.personaCommandRepository = personaCommandRepository;
        this.personaQueryRepository = personaQueryRepository;
    }

    public Persona ejecutar(Long id, byte[] foto) {
        Persona persona = personaQueryRepository.buscarPorId(id);
        if (persona == null) {
            throw new PersonaNotFoundException(id);
        }
        persona.asignarFoto(foto);
        return personaCommandRepository.update(persona);
    }
}
