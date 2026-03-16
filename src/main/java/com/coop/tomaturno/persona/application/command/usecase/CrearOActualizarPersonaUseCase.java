package com.coop.tomaturno.persona.application.command.usecase;

import java.time.LocalDateTime;

import com.coop.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.coop.tomaturno.persona.application.query.port.output.PersonaQueryRepository;
import com.coop.tomaturno.persona.dominio.entity.Persona;

public class CrearOActualizarPersonaUseCase {

    private final PersonaCommandRepository personaCommandRepository;
    private final PersonaQueryRepository personaQueryRepository;

    public CrearOActualizarPersonaUseCase(PersonaCommandRepository personaCommandRepository,
            PersonaQueryRepository personaQueryRepository) {
        this.personaCommandRepository = personaCommandRepository;
        this.personaQueryRepository = personaQueryRepository;
    }

    public Persona ejecutar(Persona persona) {
        Persona existente = personaQueryRepository.buscarPorDui(persona.getDui());

        if (existente != null) {
            existente.setNombres(persona.getNombres());
            existente.setApellidos(persona.getApellidos());
            existente.setFechaNacimiento(persona.getFechaNacimiento());
            existente.setSexo(persona.getSexo());
            existente.setFechaModificacion(LocalDateTime.now());
            return personaCommandRepository.update(existente);
        }

        persona.setFechaCreacion(LocalDateTime.now());
        return personaCommandRepository.save(persona);
    }
}
