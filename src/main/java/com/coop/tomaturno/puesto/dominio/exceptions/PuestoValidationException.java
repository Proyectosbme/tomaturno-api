package com.coop.tomaturno.puesto.dominio.exceptions;

public class PuestoValidationException extends RuntimeException {

    public PuestoValidationException(String mensaje) {
        super(mensaje);
    }

    public PuestoValidationException(String campo, String mensaje) {
        super("Validación fallida en '" + campo + "': " + mensaje);
    }
}
