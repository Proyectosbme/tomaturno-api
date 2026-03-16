package com.empresa.tomaturno.turno.application.command.service;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.turno.application.command.port.input.TurnoCommandInputPort;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.usecase.CrearTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.FinalizarTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.LlamarTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.ReasignarTurnoUseCase;
import com.empresa.tomaturno.turno.application.command.usecase.RellamarTurnoUseCase;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

public class TurnoCommandService implements TurnoCommandInputPort {

    private final CrearTurnoUseCase crearTurnoUseCase;
    private final LlamarTurnoUseCase llamarTurnoUseCase;
    private final ReasignarTurnoUseCase reasignarTurnoUseCase;
    private final FinalizarTurnoUseCase finalizarTurnoUseCase;
    private final RellamarTurnoUseCase rellamarTurnoUseCase;

    public TurnoCommandService(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository,
            ColaQueryRepository colaQueryRepository,
            ConfiguracionQueryRepository configuracionQueryRepository) {
        this.crearTurnoUseCase = new CrearTurnoUseCase(turnoCommandRepository, turnoQueryRepository, colaQueryRepository);
        this.llamarTurnoUseCase = new LlamarTurnoUseCase(turnoCommandRepository, turnoQueryRepository, configuracionQueryRepository);
        this.reasignarTurnoUseCase = new ReasignarTurnoUseCase(turnoCommandRepository, turnoQueryRepository, colaQueryRepository);
        this.finalizarTurnoUseCase = new FinalizarTurnoUseCase(turnoCommandRepository, turnoQueryRepository);
        this.rellamarTurnoUseCase = new RellamarTurnoUseCase(turnoCommandRepository, turnoQueryRepository, configuracionQueryRepository);
    }

    @Override
    public Turno crear(Long idSucursal, Long idCola, Long idDetalle, Long idPersona, Integer tipoCasoEspecial) {
        return crearTurnoUseCase.ejecutar(idSucursal, idCola, idDetalle, idPersona, tipoCasoEspecial);
    }

    @Override
    public Turno llamar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno, Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        return llamarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno, idPuesto, idSucursalPuesto, idUsuario);
    }

    @Override
    public Turno reasignar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idSucursalDestino, Long idColaDestino, Long idDetalleDestino) {
        return reasignarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno, idSucursalDestino, idColaDestino, idDetalleDestino);
    }

    @Override
    public Turno finalizar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        return finalizarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno);
    }

    @Override
    public Turno rellamar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        return rellamarTurnoUseCase.ejecutar(idSucursal, fechaCreacion, codigoTurno, idPuesto, idSucursalPuesto, idUsuario);
    }
}
