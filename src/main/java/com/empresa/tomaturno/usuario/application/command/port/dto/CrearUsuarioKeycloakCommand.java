package com.empresa.tomaturno.usuario.application.command.port.dto;

public record CrearUsuarioKeycloakCommand(
        String username,
        String nombres,
        String apellidos,
        String contrasena,
        String perfil,
        Long idSucursal
) {}
