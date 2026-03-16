package com.empresa.tomaturno.puesto.application.query.port.input;

import java.util.List;

import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

public interface PuestoQueryInputPort {
    List<Puesto> buscarPorFiltro(Long idSucursal, String nombre);
    Puesto buscarPorId(Long idPuesto, Long idSucursal);
}
