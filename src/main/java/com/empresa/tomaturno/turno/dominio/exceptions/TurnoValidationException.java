package com.empresa.tomaturno.turno.dominio.exceptions;

public class TurnoValidationException extends RuntimeException {
    public TurnoValidationException(String mensaje) {
        super(mensaje);
    }
    public TurnoValidationException(String campo, String mensaje) {
        super("Validación fallida en '" + campo + "': " + mensaje);
    }
}
