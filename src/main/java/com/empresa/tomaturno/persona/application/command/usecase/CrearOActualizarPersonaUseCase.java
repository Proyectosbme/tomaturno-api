package com.empresa.tomaturno.persona.application.command.usecase;

import com.empresa.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.empresa.tomaturno.persona.application.command.port.output.PersonaGatewayPort;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

public class CrearOActualizarPersonaUseCase {

    private final PersonaCommandRepository personaCommandRepository;
    private final PersonaGatewayPort personaGatewayPort;

    public CrearOActualizarPersonaUseCase(PersonaCommandRepository personaCommandRepository,
            PersonaGatewayPort personaGatewayPort) {
        this.personaCommandRepository = personaCommandRepository;
        this.personaGatewayPort = personaGatewayPort;
    }

    /**
     * persona ya viene construida vía Persona.of(...) con fechaCreacion estampada por el
     * llamador; si ya existe una persona con ese DUI, esa misma fecha se reutiliza como
     * fechaModificacion en vez de reconstruir vía of() (que exige fechaCreacion, no aplicable
     * a una actualización).
     */
    public Persona ejecutar(Persona persona) {
        Persona existente = personaGatewayPort.buscarPorDui(persona.getDui());

        if (existente != null) {
            existente.modificar(persona.getNombres(), persona.getApellidos(),
                    persona.getFechaNacimiento(), persona.getSexo(), persona.getFechaCreacion());
            return personaCommandRepository.update(existente);
        }

        return personaCommandRepository.save(persona);
    }
}
