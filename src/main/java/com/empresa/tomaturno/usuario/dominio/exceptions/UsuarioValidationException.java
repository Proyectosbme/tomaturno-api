package com.empresa.tomaturno.usuario.dominio.exceptions;

public class UsuarioValidationException extends RuntimeException {

    public UsuarioValidationException(String mensaje) {
        super(mensaje);
    }
}
