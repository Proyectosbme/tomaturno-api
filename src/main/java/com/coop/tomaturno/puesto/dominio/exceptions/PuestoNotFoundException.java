package com.coop.tomaturno.puesto.dominio.exceptions;

public class PuestoNotFoundException extends RuntimeException {

    public PuestoNotFoundException(String mensaje) {
        super(mensaje);
    }

    public PuestoNotFoundException(Long id, String nombreDominio) {
        super(nombreDominio + " con ID " + id + " no encontrado");
    }
}
