package com.empresa.tomaturno.framework.adapters.services;

import com.empresa.tomaturno.usuario.application.command.port.output.EncriptadoPort;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncriptadoContras implements EncriptadoPort {

    @Override
    public String encriptar(String contrasena) {
        String valor = (contrasena != null && !contrasena.isBlank()) ? contrasena : "contra123";
        return BCrypt.withDefaults().hashToString(12, valor.toCharArray());
    }

    @Override
    public boolean verificar(String contrasena, String hash) {
        return BCrypt.verifyer().verify(contrasena.toCharArray(), hash).verified;
    }
}
