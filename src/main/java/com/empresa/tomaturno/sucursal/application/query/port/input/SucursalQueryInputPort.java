package com.empresa.tomaturno.sucursal.application.query.port.input;

import java.util.List;

import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

public interface SucursalQueryInputPort {
    List<Sucursal> listarTodas();
    Sucursal buscarPorId(Long id);
    List<Sucursal> buscarPorNombre(String nombre);
}
