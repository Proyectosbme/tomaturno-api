package com.empresa.tomaturno.configuracion.application.query.port.output;

import java.util.List;

import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;

public interface ConfiguracionQueryRepository {
    List<Configuracion> buscarPorSucursal(Long idSucursal);
    Configuracion buscarPorIdYSucursal(Long idConfiguracion, Long idSucursal);
    Configuracion buscarPorNombreYSucursal(Long idSucursal, String nombre);
    Long obtenerSiguienteId(Long idSucursal);
}
