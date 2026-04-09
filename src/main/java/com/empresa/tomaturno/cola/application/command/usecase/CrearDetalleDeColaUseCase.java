package com.empresa.tomaturno.cola.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaNotFoundException;

public class CrearDetalleDeColaUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaQueryRepository colaQueryRepository;

    public CrearDetalleDeColaUseCase(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public Cola ejecutar(Long idCola, Long idSucursal, Detalle detalle,String usuario) {
        // Verificar que la cola existe
        Cola cola = colaQueryRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new ColaNotFoundException(idCola, "Cola no encontrada");
        }

        detalle.auditoriaCreacion(usuario, LocalDateTime.now());
        detalle.validarCreacion();

        // Validar que no exista otro detalle con el mismo nombre en esta cola
        boolean existeNombre = colaQueryRepository.existeNombreDetalleEnCola(
                idCola, idSucursal, detalle.getNombre());
        cola.validarNombreDetalleUnico(detalle.getNombre(), existeNombre);

        return colaCommandRepository.guardarDetalle(idCola, idSucursal, detalle);
    }
}
