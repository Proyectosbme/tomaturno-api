package com.empresa.tomaturno.empresa.application.command.usecase;

import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaCommandRepository;
import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaGatewayPort;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.empresa.dominio.exceptions.EmpresaNotFoundException;

public class ActualizarNombreEmpresaUseCase {

    private final EmpresaCommandRepository commandRepository;
    private final EmpresaGatewayPort empresaGatewayPort;

    public ActualizarNombreEmpresaUseCase(EmpresaCommandRepository commandRepository,
                                           EmpresaGatewayPort empresaGatewayPort) {
        this.commandRepository = commandRepository;
        this.empresaGatewayPort = empresaGatewayPort;
    }

    public Empresa ejecutar(String nombre) {
        Empresa empresa = empresaGatewayPort.obtener();
        if (empresa == null) {
            throw new EmpresaNotFoundException("No se encontró la configuración de la empresa");
        }
        empresa.actualizarNombre(nombre);
        return commandRepository.actualizar(empresa);
    }
}
