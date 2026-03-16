package com.empresa.tomaturno.configuracion.dominio.exceptions;

public class ConfiguracionValidationException extends RuntimeException {
    public ConfiguracionValidationException(String message) {
        super(message);
    }
}
