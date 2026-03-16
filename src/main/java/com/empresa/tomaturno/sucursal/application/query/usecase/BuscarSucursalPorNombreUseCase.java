package com.empresa.tomaturno.sucursal.application.query.usecase;

import java.util.List;

import com.empresa.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

public class BuscarSucursalPorNombreUseCase {

    private final SucursalQueryRepository sucursalRepository;

    public BuscarSucursalPorNombreUseCase(SucursalQueryRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public List<Sucursal> ejecutar(String nombre) {
        return sucursalRepository.buscarPorNombre(nombre);
    }
}
