package com.empresa.tomaturno.estadooperador.application.command.service;

import com.empresa.tomaturno.estadooperador.application.command.port.input.EstadoOperadorCommandInputPort;
import com.empresa.tomaturno.estadooperador.application.command.port.output.EstadoOperadorCommandRepository;
import com.empresa.tomaturno.estadooperador.application.command.port.output.EstadoOperadorGatewayPort;
import com.empresa.tomaturno.estadooperador.application.command.usecase.AbrirOperadorUseCase;
import com.empresa.tomaturno.estadooperador.application.command.usecase.CerrarOperadorUseCase;
import com.empresa.tomaturno.estadooperador.application.command.usecase.IniciarDescansoUseCase;
import com.empresa.tomaturno.estadooperador.application.command.usecase.QuitarDescansoUseCase;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;

public class EstadoOperadorCommandService implements EstadoOperadorCommandInputPort {

    private final AbrirOperadorUseCase abrirOperadorUseCase;
    private final CerrarOperadorUseCase cerrarOperadorUseCase;
    private final IniciarDescansoUseCase iniciarDescansoUseCase;
    private final QuitarDescansoUseCase quitarDescansoUseCase;

    public EstadoOperadorCommandService(EstadoOperadorCommandRepository commandRepository,
                                     EstadoOperadorGatewayPort estadoOperadorGatewayPort) {
        this.abrirOperadorUseCase = new AbrirOperadorUseCase(commandRepository, estadoOperadorGatewayPort);
        this.cerrarOperadorUseCase = new CerrarOperadorUseCase(commandRepository, estadoOperadorGatewayPort);
        this.iniciarDescansoUseCase = new IniciarDescansoUseCase(commandRepository, estadoOperadorGatewayPort);
        this.quitarDescansoUseCase = new QuitarDescansoUseCase(commandRepository, estadoOperadorGatewayPort);
    }

    @Override
    public EstadoOperador abrirOperador(Long idUsuario, Long idSucursal, Long idPuesto) {
        return abrirOperadorUseCase.ejecutar(idUsuario, idSucursal, idPuesto);
    }

    @Override
    public EstadoOperador cerrarOperador(Long idUsuario, Long idSucursal, Long idPuesto) {
        return cerrarOperadorUseCase.ejecutar(idUsuario, idSucursal, idPuesto);
    }

    @Override
    public EstadoOperador iniciarDescanso(Long idUsuario, Long idSucursal, Long idPuesto, Long idTipoDescanso, String comentario) {
        return iniciarDescansoUseCase.ejecutar(idUsuario, idSucursal, idPuesto, idTipoDescanso, comentario);
    }

    @Override
    public EstadoOperador quitarDescanso(Long idUsuario, Long idSucursal, Long idPuesto) {
        return quitarDescansoUseCase.ejecutar(idUsuario, idSucursal, idPuesto);
    }
}
