package com.coop.tomaturno.sucursal.application.query.service;

import java.util.List;

import com.coop.tomaturno.sucursal.application.query.port.input.SucursalQueryInputPort;
import com.coop.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.coop.tomaturno.sucursal.application.query.usecase.BuscarSucursalPorIdUseCase;
import com.coop.tomaturno.sucursal.application.query.usecase.BuscarSucursalPorNombreUseCase;
import com.coop.tomaturno.sucursal.application.query.usecase.ListarTodasLasSucursalesUseCase;
import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;

public class SucursalQueryService implements SucursalQueryInputPort {

    private final ListarTodasLasSucursalesUseCase listarTodasLasSucursalesUseCase;
    private final BuscarSucursalPorIdUseCase buscarSucursalPorIdUseCase;
    private final BuscarSucursalPorNombreUseCase buscarSucursalPorNombreUseCase;

    public SucursalQueryService(SucursalQueryRepository sucursalQueryRepository) {
        this.listarTodasLasSucursalesUseCase = new ListarTodasLasSucursalesUseCase(sucursalQueryRepository);
        this.buscarSucursalPorIdUseCase = new BuscarSucursalPorIdUseCase(sucursalQueryRepository);
        this.buscarSucursalPorNombreUseCase = new BuscarSucursalPorNombreUseCase(sucursalQueryRepository);
    }

    @Override
    public List<Sucursal> listarTodas() {
        return listarTodasLasSucursalesUseCase.ejecutar();
    }

    @Override
    public Sucursal buscarPorId(Long id) {
        return buscarSucursalPorIdUseCase.ejecutar(id);
    }

    @Override
    public List<Sucursal> buscarPorNombre(String nombre) {
        return buscarSucursalPorNombreUseCase.ejecutar(nombre);
    }
}
