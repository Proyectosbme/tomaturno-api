package com.empresa.tomaturno.sucursal.application.query.port.output;

import java.util.List;

import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

public interface SucursalQueryRepository {

    List<Sucursal> listarTodas();

    Sucursal buscarPorId(Long id);

    List<Sucursal> buscarPorNombre(String nombre);

}
