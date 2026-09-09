package com.empresa.tomaturno.empresa.application.command.usecase;

import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaCommandRepository;
import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaGatewayPort;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.empresa.dominio.exceptions.EmpresaNotFoundException;

public class ActualizarBannerEmpresaUseCase {

    private final EmpresaCommandRepository commandRepository;
    private final EmpresaGatewayPort empresaGatewayPort;

    public ActualizarBannerEmpresaUseCase(EmpresaCommandRepository commandRepository,
                                           EmpresaGatewayPort empresaGatewayPort) {
        this.commandRepository = commandRepository;
        this.empresaGatewayPort = empresaGatewayPort;
    }

    public Empresa ejecutar(byte[] banner) {
        Empresa empresa = empresaGatewayPort.obtener();
        if (empresa == null) {
            throw new EmpresaNotFoundException("No se encontró la configuración de la empresa");
        }
        empresa.actualizarBanner(banner);
        return commandRepository.actualizar(empresa);
    }
}
