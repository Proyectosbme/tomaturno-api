package com.empresa.tomaturno.empresa.application.command.port.input;

import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

public interface EmpresaCommandInputPort {
    Empresa actualizarNombre(String nombre);
    Empresa actualizarBanner(byte[] banner);
    Empresa actualizarLogo(byte[] logo);
}
