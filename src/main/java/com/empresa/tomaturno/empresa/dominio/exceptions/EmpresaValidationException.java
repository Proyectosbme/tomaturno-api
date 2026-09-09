package com.empresa.tomaturno.empresa.dominio.exceptions;

public class EmpresaValidationException extends RuntimeException {
    public EmpresaValidationException(String message) {
        super(message);
    }
}
