package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoColaPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class ReasignarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;
    private final TurnoColaPort turnoColaPort;

    public ReasignarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository,
            TurnoColaPort turnoColaPort) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
        this.turnoColaPort = turnoColaPort;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idSucursalDestino, Long idColaDestino, Long idDetalleDestino) {
        Turno original = turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (original == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno);
        }

        Long idDetalleValido = turnoColaPort.resolverDetalleParaReasignacion(idColaDestino, idSucursalDestino, idDetalleDestino);
        if (idDetalleValido == null) {
            throw new TurnoValidationException("Cola destino no encontrada: idCola=" + idColaDestino);
        }

        Turno nuevo = original.reasignarA(
                turnoQueryRepository.obtenerSiguienteId(),
                idSucursalDestino, idColaDestino, idDetalleValido);
        return turnoCommandRepository.reasignar(original, nuevo);
    }
}
