package com.coop.tomaturno.usuario.dominio.exceptions;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(String mensaje) {
        super(mensaje);
    }

    public UsuarioNotFoundException(Long id, String contexto) {
        super("Usuario no encontrado con id: " + id + " en " + contexto);
    }
}
