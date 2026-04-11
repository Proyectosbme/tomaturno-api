package com.empresa.tomaturno.empresa.application.command.usecase;

import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaCommandRepository;
import com.empresa.tomaturno.empresa.application.query.port.output.EmpresaQueryRepository;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.empresa.dominio.exceptions.EmpresaNotFoundException;

public class ActualizarBannerEmpresaUseCase {

    private final EmpresaCommandRepository commandRepository;
    private final EmpresaQueryRepository queryRepository;

    public ActualizarBannerEmpresaUseCase(EmpresaCommandRepository commandRepository,
                                           EmpresaQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Empresa ejecutar(byte[] banner) {
        Empresa empresa = queryRepository.obtener();
        if (empresa == null) {
            throw new EmpresaNotFoundException("No se encontró la configuración de la empresa");
        }
        empresa.actualizarBanner(banner);
        return commandRepository.actualizar(empresa);
    }
}
