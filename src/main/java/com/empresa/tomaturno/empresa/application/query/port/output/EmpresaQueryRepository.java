package com.empresa.tomaturno.empresa.application.query.port.output;

import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

public interface EmpresaQueryRepository {
    Empresa obtener();
}
