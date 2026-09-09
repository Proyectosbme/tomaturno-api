package com.empresa.tomaturno.framework.adapters.output.persistencia.adapters;

import com.empresa.tomaturno.empresa.application.command.port.output.EmpresaGatewayPort;
import com.empresa.tomaturno.empresa.application.query.port.output.EmpresaQueryRepository;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

public class EmpresaGatewayAdapter implements EmpresaGatewayPort {

    private final EmpresaQueryRepository empresaQueryRepository;

    public EmpresaGatewayAdapter(EmpresaQueryRepository empresaQueryRepository) {
        this.empresaQueryRepository = empresaQueryRepository;
    }

    @Override
    public Empresa obtener() {
        return empresaQueryRepository.obtener();
    }
}
