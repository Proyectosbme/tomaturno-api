package com.empresa.tomaturno.empresa.dominio.exceptions;

public class EmpresaNotFoundException extends RuntimeException {

    public EmpresaNotFoundException(String mensaje) {
        super(mensaje);
    }
}
