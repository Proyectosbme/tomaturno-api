package com.coop.tomaturno.configuracion.application.query.port.input;

import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;

import java.util.List;

public interface ConfiguracionQueryInputPort {
    List<Configuracion> buscarPorSucursal(Long idSucursal);
    Configuracion buscarPorId(Long idConfiguracion, Long idSucursal);
    Configuracion buscarPorNombre(Long idSucursal, String nombre);
}
