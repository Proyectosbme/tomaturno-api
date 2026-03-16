package com.empresa.tomaturno.configuracion.application.query.port.input;

import java.util.List;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public interface ConfiguracionQueryInputPort {
    List<Configuracion> buscarPorSucursal(Long idSucursal);
    Configuracion buscarPorId(Long idConfiguracion, Long idSucursal);
    Configuracion buscarPorNombre(Long idSucursal, String nombre);
}
