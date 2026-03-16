package com.empresa.tomaturno.cola.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaNotFoundException;

public class ModificarColaUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaQueryRepository colaQueryRepository;

    public ModificarColaUseCase(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public Cola ejecutar(Long idCola, Long idSucursal, Cola datosNuevos) {
        Cola cola = colaQueryRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new ColaNotFoundException(idCola,
                    "Cola (idCola=" + idCola + ", idSucursal=" + idSucursal + ")");
        }
        cola.auditoriaModificacion("bmarroquin", LocalDateTime.now());
        cola.modificar(datosNuevos.getNombre(),
                datosNuevos.getCodigo(),
                datosNuevos.getPrioridad(),
                datosNuevos.getEstado());
        cola.validarModificacion();
        return colaCommandRepository.modificar(cola);
    }
}
