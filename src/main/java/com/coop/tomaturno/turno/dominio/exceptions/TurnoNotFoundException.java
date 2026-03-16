package com.coop.tomaturno.turno.dominio.exceptions;

public class TurnoNotFoundException extends RuntimeException {
    public TurnoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
