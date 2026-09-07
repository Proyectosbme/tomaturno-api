package com.empresa.tomaturno.turno.application.command.service;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.input.LlamarTurnoInputPort;
import com.empresa.tomaturno.turno.application.command.port.input.TurnoCommandInputPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoColaPort;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.usecase.CrearTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.FinalizarTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.LlamarTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.MarcarEnEsperaUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.ReasignarTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.LlamarSiguienteTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.MarcarSinAtenderUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.RellamarTurnoUseCase;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoConfiguracionPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

public class TurnoCommandService implements TurnoCommandInputPort, LlamarTurnoInputPort {

    private final CrearTurnoUseCase crearTurnoUseCase;
    private final LlamarTurnoUseCase llamarTurnoUseCase;
    private final LlamarSiguienteTurnoUseCase llamarSiguienteTurnoUseCase;
    private final MarcarSinAtenderUseCase marcarSinAtenderUseCase;
    private final ReasignarTurnoUseCase reasignarTurnoUseCase;
    private final FinalizarTurnoUseCase finalizarTurnoUseCase;
    private final RellamarTurnoUseCase rellamarTurnoUseCase;
    private final MarcarEnEsperaUseCase marcarEnEsperaUseCase;

    public TurnoCommandService(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository,
            TurnoColaPort turnoColaPort,
            TurnoConfiguracionPort turnoConfiguracionPort) {
        this.crearTurnoUseCase = new CrearTurnoUseCase(turnoCommandRepository, turnoQueryRepository);
        this.llamarTurnoUseCase = new LlamarTurnoUseCase(turnoCommandRepository, turnoQueryRepository,
                turnoConfiguracionPort);
        this.llamarSiguienteTurnoUseCase = new LlamarSiguienteTurnoUseCase(turnoQueryRepository, this);
        this.marcarSinAtenderUseCase = new MarcarSinAtenderUseCase(turnoCommandRepository, turnoQueryRepository);
        this.reasignarTurnoUseCase = new ReasignarTurnoUseCase(turnoCommandRepository, turnoQueryRepository,
                turnoColaPort);
        this.finalizarTurnoUseCase = new FinalizarTurnoUseCase(turnoCommandRepository, turnoQueryRepository);
        this.rellamarTurnoUseCase = new RellamarTurnoUseCase(turnoCommandRepository, turnoQueryRepository,
                turnoConfiguracionPort);
        this.marcarEnEsperaUseCase = new MarcarEnEsperaUseCase(turnoCommandRepository, turnoQueryRepository);
    }

    @Override
    public Turno crear(Long idSucursal, Long idCola, Long idDetalle, Long idPersona, Integer tipoCasoEspecial) {
        return crearTurnoUseCase.ejecutar(idSucursal, idCola, idDetalle, idPersona, tipoCasoEspecial);
    }

    @Override
    public Turno llamar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno, Long idPuesto,
            Long idSucursalPuesto, Long idUsuario) {
        return llamarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno, idPuesto, idSucursalPuesto,
                idUsuario);
    }

    @Override
    public Turno reasignar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idSucursalDestino, Long idColaDestino, Long idDetalleDestino) {
        return reasignarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno, idSucursalDestino, idColaDestino,
                idDetalleDestino);
    }

    @Override
    public Turno sinAtender(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return marcarSinAtenderUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno);
    }

    @Override
    public Turno finalizar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return finalizarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno);
    }

    @Override
    public Turno llamarSiguiente(Long idSucursal, Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        return llamarSiguienteTurnoUseCase.ejecutar(idSucursal, idPuesto, idSucursalPuesto, idUsuario);
    }

    @Override
    public Turno rellamar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        return rellamarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno, idPuesto, idSucursalPuesto,
                idUsuario);
    }

    @Override
    public Turno enEspera(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
       return marcarEnEsperaUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno);
    }
}
