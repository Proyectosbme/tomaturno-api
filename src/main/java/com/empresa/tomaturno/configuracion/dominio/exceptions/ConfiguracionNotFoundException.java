package com.empresa.tomaturno.configuracion.dominio.exceptions;

public class ConfiguracionNotFoundException extends RuntimeException {
    public ConfiguracionNotFoundException(String message) {
        super(message);
    }
}
