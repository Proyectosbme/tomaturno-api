package com.empresa.tomaturno.persona.dominio.exceptions;

public class PersonaValidationException extends RuntimeException {

    public PersonaValidationException(String message) {
        super(message);
    }
}
