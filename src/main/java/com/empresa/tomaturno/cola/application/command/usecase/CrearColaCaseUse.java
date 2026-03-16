package com.empresa.tomaturno.cola.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;

public class CrearColaCaseUse {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaQueryRepository colaQueryRepository;

    public CrearColaCaseUse(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public Cola ejecutar(Cola cola) {
        cola.auditoriaCreacion("bmarroquin", LocalDateTime.now());
        cola.validarCreacion();

        // Validar que no exista otra cola con el mismo nombre en la misma sucursal
        boolean existeNombre = colaQueryRepository.existeNombreEnSucursal(
                cola.getSucursal().getIdentificador(), cola.getNombre());
        cola.validarNombreUnico(existeNombre);

        return colaCommandRepository.save(cola);
    }
}
