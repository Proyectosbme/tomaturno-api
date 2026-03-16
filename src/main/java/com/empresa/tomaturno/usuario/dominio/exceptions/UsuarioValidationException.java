package com.coop.tomaturno.usuario.dominio.exceptions;

public class UsuarioValidationException extends RuntimeException {

    public UsuarioValidationException(String mensaje) {
        super(mensaje);
    }
}
