package com.empresa.tomaturno.empresa.application.query.usecase;

import com.empresa.tomaturno.empresa.application.query.port.output.EmpresaQueryRepository;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.empresa.dominio.exceptions.EmpresaNotFoundException;

public class ObtenerEmpresaUseCase {

    private final EmpresaQueryRepository queryRepository;

    public ObtenerEmpresaUseCase(EmpresaQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Empresa ejecutar() {
        Empresa empresa = queryRepository.obtener();
        if (empresa == null) {
            throw new EmpresaNotFoundException("No se encontró la configuración de la empresa");
        }
        return empresa;
    }
}
