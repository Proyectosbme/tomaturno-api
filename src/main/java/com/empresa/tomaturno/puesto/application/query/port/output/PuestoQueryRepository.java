package com.empresa.tomaturno.puesto.application.query.port.output;

import java.util.List;

import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public interface PuestoQueryRepository {
    Puesto buscarPorIdPuestoYSucursal(Long idPuesto, Long idSucursal);
    List<Puesto> buscarPorFiltro(Long idSucursal, String nombre);
    boolean existeNombreEnSucursal(Long idSucursal, String nombre);
}
