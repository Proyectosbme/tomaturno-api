package com.empresa.tomaturno.empresa.application.query.service;

import com.empresa.tomaturno.empresa.application.query.port.input.EmpresaQueryInputPort;
import com.empresa.tomaturno.empresa.application.query.port.output.EmpresaQueryRepository;
import com.empresa.tomaturno.empresa.application.query.usecase.ObtenerEmpresaUseCase;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

public class EmpresaQueryService implements EmpresaQueryInputPort {

    private final ObtenerEmpresaUseCase obtenerEmpresaUseCase;

    public EmpresaQueryService(EmpresaQueryRepository queryRepository) {
        this.obtenerEmpresaUseCase = new ObtenerEmpresaUseCase(queryRepository);
    }

    @Override
    public Empresa obtener() {
        return obtenerEmpresaUseCase.ejecutar();
    }
}
