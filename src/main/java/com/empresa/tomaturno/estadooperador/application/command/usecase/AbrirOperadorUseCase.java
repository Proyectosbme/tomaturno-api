package com.empresa.tomaturno.estadooperador.application.command.usecase;

import com.empresa.tomaturno.estadooperador.application.command.port.output.EstadoOperadorCommandRepository;
import com.empresa.tomaturno.estadooperador.application.query.port.output.EstadoOperadorQueryRepository;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public class AbrirOperadorUseCase {

    private final EstadoOperadorCommandRepository commandRepository;
    private final EstadoOperadorQueryRepository queryRepository;

    public AbrirOperadorUseCase(EstadoOperadorCommandRepository commandRepository, EstadoOperadorQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public EstadoOperador ejecutar(Long idUsuario, Long idSucursal, Long idPuesto) {
        EstadoOperador vigente = queryRepository.buscarVigente(idUsuario, idSucursal);
        if (vigente != null) {
            vigente.cerrarVigencia();
            commandRepository.actualizar(vigente);
        }
        EstadoOperador nueva = EstadoOperador.abrir(idUsuario, idSucursal, idPuesto);
        // TODO(auto-llamado): el llamado/cierre automático del turno activo se orquesta en el controller
        return commandRepository.guardar(nueva);
    }
}
