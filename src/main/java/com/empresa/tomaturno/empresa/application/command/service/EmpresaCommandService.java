package com.empresa.tomaturno.empresa.application.command.service;

import com.empresa.tomaturno.empresa.application.command.port.input.EmpresaCommandInputPort;
import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaCommandRepository;
import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaGatewayPort;
import com.empresa.tomaturno.empresa.application.command.usecase.ActualizarBannerEmpresaUseCase;
import com.empresa.tomaturno.empresa.application.command.usecase.ActualizarLogoEmpresaUseCase;
import com.empresa.tomaturno.empresa.application.command.usecase.ActualizarNombreEmpresaUseCase;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

public class EmpresaCommandService implements EmpresaCommandInputPort {

    private final ActualizarNombreEmpresaUseCase actualizarNombreUseCase;
    private final ActualizarBannerEmpresaUseCase actualizarBannerUseCase;
    private final ActualizarLogoEmpresaUseCase actualizarLogoUseCase;

    public EmpresaCommandService(EmpresaCommandRepository commandRepository,
                                  EmpresaGatewayPort empresaGatewayPort) {
        this.actualizarNombreUseCase = new ActualizarNombreEmpresaUseCase(commandRepository, empresaGatewayPort);
        this.actualizarBannerUseCase = new ActualizarBannerEmpresaUseCase(commandRepository, empresaGatewayPort);
        this.actualizarLogoUseCase = new ActualizarLogoEmpresaUseCase(commandRepository, empresaGatewayPort);
    }

    @Override
    public Empresa actualizarNombre(String nombre) {
        return actualizarNombreUseCase.ejecutar(nombre);
    }

    @Override
    public Empresa actualizarBanner(byte[] banner) {
        return actualizarBannerUseCase.ejecutar(banner);
    }

    @Override
    public Empresa actualizarLogo(byte[] logo) {
        return actualizarLogoUseCase.ejecutar(logo);
    }
}
