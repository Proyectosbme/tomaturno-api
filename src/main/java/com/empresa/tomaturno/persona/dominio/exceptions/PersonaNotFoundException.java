package com.empresa.tomaturno.persona.dominio.exceptions;

public class PersonaNotFoundException extends RuntimeException {

    public PersonaNotFoundException(Long id) {
        super("Persona no encontrada con id: " + id);
    }
}
