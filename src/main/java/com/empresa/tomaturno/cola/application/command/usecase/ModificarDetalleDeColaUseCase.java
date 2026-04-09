package com.empresa.tomaturno.cola.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaNotFoundException;

public class ModificarDetalleDeColaUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaQueryRepository colaQueryRepository;

    public ModificarDetalleDeColaUseCase(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public Cola ejecutar(Long idCola, Long idSucursal, Long idDetalle, Detalle detalle,String usuario) {
        // Verificar que la cola existe
        Detalle modificado = colaQueryRepository.obtenerDetalle(idCola, idSucursal, idDetalle);
        if (modificado == null) {
            throw new ColaNotFoundException(idCola, "Detalle no encontrado");
        }

        modificado.modificar(detalle.getNombre(), detalle.getCodigo(), detalle.getEstado(), usuario, LocalDateTime.now());
        modificado.validarModificacion();
        return colaCommandRepository.modificarDetalle(idCola, idSucursal, modificado);
    }
}
