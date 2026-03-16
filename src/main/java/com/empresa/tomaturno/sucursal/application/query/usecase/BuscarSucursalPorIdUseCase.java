package com.empresa.tomaturno.sucursal.application.query.usecase;

import com.empresa.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalNotFoundException;

public class BuscarSucursalPorIdUseCase {

    private final SucursalQueryRepository sucursalRepository;

    public BuscarSucursalPorIdUseCase(SucursalQueryRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public Sucursal ejecutar(Long id) {
        Sucursal sucursal = sucursalRepository.buscarPorId(id);
        if (sucursal == null) {
            throw new SucursalNotFoundException(id, "Sucursal");
        }
        return sucursal;
    }
}
