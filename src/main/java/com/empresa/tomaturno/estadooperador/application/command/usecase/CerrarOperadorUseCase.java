package com.empresa.tomaturno.estadooperador.application.command.usecase;

import com.empresa.tomaturno.estadooperador.application.command.port.output.EstadoOperadorCommandRepository;
import com.empresa.tomaturno.estadooperador.application.command.port.output.EstadoOperadorGatewayPort;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public class CerrarOperadorUseCase {

    private final EstadoOperadorCommandRepository commandRepository;
    private final EstadoOperadorGatewayPort estadoOperadorGatewayPort;

    public CerrarOperadorUseCase(EstadoOperadorCommandRepository commandRepository,
            EstadoOperadorGatewayPort estadoOperadorGatewayPort) {
        this.commandRepository = commandRepository;
        this.estadoOperadorGatewayPort = estadoOperadorGatewayPort;
    }

    public EstadoOperador ejecutar(Long idUsuario, Long idSucursal, Long idPuesto) {
        EstadoOperador vigente = estadoOperadorGatewayPort.buscarVigente(idUsuario, idSucursal);
        if (vigente != null) {
            vigente.cerrarVigencia();
            commandRepository.actualizar(vigente);
        }
        EstadoOperador nueva = EstadoOperador.cerrar(idUsuario, idSucursal, idPuesto);
        // TODO(auto-llamado): el llamado/cierre automático del turno activo se orquesta en el controller
        return commandRepository.guardar(nueva);
    }
}
