package com.empresa.tomaturno.usuario.application.command.port.output;

public interface EncriptadoPort {

    String encriptar(String contrasena);

    boolean verificar(String contrasena, String hash);
}
