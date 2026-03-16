package com.empresa.tomaturno.detallecolaxpuesto.application.command.usecase;

import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.exceptions.DetalleColaxPuestoNotFoundException;

public class DesasignarDetalleColaPuestoUseCase {

    private final DetalleColaxPuestoCommandRepository commandRepository;
    private final DetalleColaxPuestoQueryRepository queryRepository;

    public DesasignarDetalleColaPuestoUseCase(DetalleColaxPuestoCommandRepository commandRepository,
                                               DetalleColaxPuestoQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public void ejecutar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola) {
        boolean existe = queryRepository.existeAsignacion(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
        if (!existe) {
            throw new DetalleColaxPuestoNotFoundException(
                    "No se encontró la asignación con los parámetros indicados");
        }
        commandRepository.eliminar(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
    }
}
