package com.empresa.tomaturno.empresa.application.command.service;

import com.empresa.tomaturno.empresa.application.command.port.input.EmpresaCommandInputPort;
import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaCommandRepository;
import com.empresa.tomaturno.empresa.application.command.usecase.ActualizarBannerEmpresaUseCase;
import com.empresa.tomaturno.empresa.application.command.usecase.ActualizarLogoEmpresaUseCase;
import com.empresa.tomaturno.empresa.application.command.usecase.ActualizarNombreEmpresaUseCase;
import com.empresa.tomaturno.empresa.application.query.port.output.EmpresaQueryRepository;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

public class EmpresaCommandService implements EmpresaCommandInputPort {

    private final ActualizarNombreEmpresaUseCase actualizarNombreUseCase;
    private final ActualizarBannerEmpresaUseCase actualizarBannerUseCase;
    private final ActualizarLogoEmpresaUseCase actualizarLogoUseCase;

    public EmpresaCommandService(EmpresaCommandRepository commandRepository,
                                  EmpresaQueryRepository queryRepository) {
        this.actualizarNombreUseCase = new ActualizarNombreEmpresaUseCase(commandRepository, queryRepository);
        this.actualizarBannerUseCase = new ActualizarBannerEmpresaUseCase(commandRepository, queryRepository);
        this.actualizarLogoUseCase = new ActualizarLogoEmpresaUseCase(commandRepository, queryRepository);
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
