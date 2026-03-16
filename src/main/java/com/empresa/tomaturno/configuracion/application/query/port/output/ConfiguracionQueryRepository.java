package com.coop.tomaturno.configuracion.application.query.port.output;

import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;

import java.util.List;

public interface ConfiguracionQueryRepository {
    List<Configuracion> buscarPorSucursal(Long idSucursal);
    Configuracion buscarPorIdYSucursal(Long idConfiguracion, Long idSucursal);
    Configuracion buscarPorNombreYSucursal(Long idSucursal, String nombre);
    Long obtenerSiguienteId(Long idSucursal);
}
