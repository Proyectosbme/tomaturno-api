package com.empresa.tomaturno.detallecolaxpuesto.application.command.service;

import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.input.DetalleColaxPuestoCommandInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.usecase.AsignarDetalleColaPuestoUseCase;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.usecase.DesasignarDetalleColaPuestoUseCase;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.usecase.ModificarPrioridadUseCase;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;

public class DetalleColaxPuestoCommandService implements DetalleColaxPuestoCommandInputPort {

    private final AsignarDetalleColaPuestoUseCase asignarUseCase;
    private final DesasignarDetalleColaPuestoUseCase desasignarUseCase;
    private final ModificarPrioridadUseCase modificarPrioridadUseCase;

    public DetalleColaxPuestoCommandService(DetalleColaxPuestoCommandRepository commandRepository,
                                             DetalleColaxPuestoQueryRepository queryRepository) {
        this.asignarUseCase = new AsignarDetalleColaPuestoUseCase(commandRepository, queryRepository);
        this.desasignarUseCase = new DesasignarDetalleColaPuestoUseCase(commandRepository, queryRepository);
        this.modificarPrioridadUseCase = new ModificarPrioridadUseCase(commandRepository, queryRepository);
    }

    @Override
    public DetalleColaxPuesto asignar(DetalleColaxPuesto asignacion, String usuario) {
        return asignarUseCase.ejecutar(asignacion, usuario);
    }

    @Override
    public void desasignar(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle, Long idSucursalCola) {
        desasignarUseCase.ejecutar(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
    }

    @Override
    public DetalleColaxPuesto modificarPrioridad(Long idPuesto, Long idSucursalPuesto, Long idCola, Long idDetalle,
            Long idSucursalCola, Long prioridad) {
       return this.modificarPrioridadUseCase.ejecutar(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola, prioridad);
    }
}
