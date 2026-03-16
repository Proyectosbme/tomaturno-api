package com.empresa.tomaturno.sucursal.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

public class CrearSucursalCaseUse {

    private final SucursalCommandRepository sucursalCommandRepository;

    public CrearSucursalCaseUse(SucursalCommandRepository sucursalCommandRepository) {
        this.sucursalCommandRepository = sucursalCommandRepository;
    }

    public Sucursal ejecutar(Sucursal sucursal) {
        sucursal.auditoriaCreacion("bmarroquin", LocalDateTime.now());
        sucursal.validarCreacion();
        return sucursalCommandRepository.save(sucursal);
    }

}
