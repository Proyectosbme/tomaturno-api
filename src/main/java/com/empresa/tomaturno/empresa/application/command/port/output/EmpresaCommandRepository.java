package com.empresa.tomaturno.empresa.application.command.port.output;

import com.empresa.tomaturno.empresa.dominio.entity.Empresa;

public interface EmpresaCommandRepository {
    Empresa actualizar(Empresa empresa);
}
