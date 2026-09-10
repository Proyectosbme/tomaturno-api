package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoGatewayPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class ReasignarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoGatewayPort turnoGatewayPort;

    public ReasignarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoGatewayPort turnoGatewayPort) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoGatewayPort = turnoGatewayPort;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idSucursalDestino, Long idColaDestino, Long idDetalleDestino) {
        Turno original = turnoGatewayPort.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (original == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno);
        }

        Long idDetalleValido = turnoGatewayPort.resolverDetalleParaReasignacion(idColaDestino, idSucursalDestino, idDetalleDestino);
        if (idDetalleValido == null) {
            throw new TurnoValidationException("Cola destino no encontrada: idCola=" + idColaDestino);
        }

        Turno nuevo = original.reasignarA(
                turnoGatewayPort.obtenerSiguienteId(),
                idSucursalDestino, idColaDestino, idDetalleValido);
        return turnoCommandRepository.reasignar(original, nuevo);
    }
}
