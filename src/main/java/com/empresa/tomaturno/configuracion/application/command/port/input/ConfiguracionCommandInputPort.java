package com.empresa.tomaturno.configuracion.application.command.port.input;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.vo.Auditoria;
import com.empresa.tomaturno.configuracion.dominio.vo.Estado;

public interface ConfiguracionCommandInputPort {
    Configuracion crear(Configuracion configuracion);
    Configuracion actualizar(Long idConfiguracion, Long idSucursal, Integer parametro, String descripcion,
            Estado estado, Auditoria auditoriaModificacion);
}
