package com.empresa.tomaturno.sucursal.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.empresa.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalNotFoundException;

public class ModificarSucursalUseCase {

    private final SucursalCommandRepository sucursalCommandRepository;
    private final SucursalQueryRepository sucursalQueryRepository;

    public ModificarSucursalUseCase(SucursalCommandRepository sucursalCommandRepository,
            SucursalQueryRepository sucursalQueryRepository) {
        this.sucursalCommandRepository = sucursalCommandRepository;
        this.sucursalQueryRepository = sucursalQueryRepository;
    }

    public Sucursal ejecutar(Long id, Sucursal datosNuevos) {
        Sucursal sucursal = sucursalQueryRepository.buscarPorId(id);
        if (sucursal != null) {
            sucursal.auditoriaModificacion("bmarroquin", LocalDateTime.now());
            sucursal.modificar(datosNuevos.getNombre(), datosNuevos.getContacto(), datosNuevos.getEstado());
            return sucursalCommandRepository.modificar(sucursal);

        }
        throw new SucursalNotFoundException(id, "Sucursal");

    }
}
