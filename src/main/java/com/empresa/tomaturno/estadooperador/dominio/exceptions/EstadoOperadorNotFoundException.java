package com.empresa.tomaturno.estadooperador.dominio.exceptions;

public class EstadoOperadorNotFoundException extends RuntimeException {
    public EstadoOperadorNotFoundException(String message) {
        super(message);
    }
}
