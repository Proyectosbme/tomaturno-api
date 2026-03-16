package com.coop.tomaturno.detallecolaxpuesto.application.command.usecase;

import java.time.LocalDateTime;

import com.coop.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.coop.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.coop.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.coop.tomaturno.detallecolaxpuesto.dominio.exceptions.DetalleColaxPuestoValidationException;

public class AsignarDetalleColaPuestoUseCase {

    private final DetalleColaxPuestoCommandRepository commandRepository;
    private final DetalleColaxPuestoQueryRepository queryRepository;

    public AsignarDetalleColaPuestoUseCase(DetalleColaxPuestoCommandRepository commandRepository,
                                            DetalleColaxPuestoQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public DetalleColaxPuesto ejecutar(DetalleColaxPuesto asignacion) {
        asignacion.validarAsignacion();

        boolean existe = queryRepository.existeAsignacion(
                asignacion.getIdPuesto(), asignacion.getIdSucursalPuesto(),
                asignacion.getIdCola(), asignacion.getIdDetalle(), asignacion.getIdSucursalCola());

        if (existe) {
            throw new DetalleColaxPuestoValidationException(
                    "Ya existe la asignación de este detalle de cola al puesto indicado");
        }

        asignacion.auditoriaCreacion("bmarroquin", LocalDateTime.now());
        return commandRepository.save(asignacion);
    }
}
