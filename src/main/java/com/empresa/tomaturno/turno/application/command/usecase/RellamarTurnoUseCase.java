package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoGatewayPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class RellamarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoGatewayPort turnoGatewayPort;

    public RellamarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoGatewayPort turnoGatewayPort) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoGatewayPort = turnoGatewayPort;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idPuesto, Long idSucursalPuesto, Long idUsuario) {

        Turno turno = turnoGatewayPort.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (turno == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno);
        }

        if (idUsuario != null && !turnoGatewayPort.operadorActivo(idUsuario, idSucursal)) {
            throw new TurnoValidationException("No se puede llamar un turno: el operador no está activo. Actívate primero.");
        }

        if (turnoGatewayPort.debeVerificarTurnoActivo(idSucursal)) {
            boolean tieneActivo = (idUsuario != null)
                    ? turnoGatewayPort.existeTurnoLlamadoPorUsuario(idUsuario, idSucursal, LocalDate.now())
                    : turnoGatewayPort.existeTurnoLlamadoPorPuesto(idPuesto, idSucursal, LocalDate.now());
            if (tieneActivo) {
                throw new TurnoValidationException("El operador ya tiene un turno activo. Finalícelo antes de volver a llamar.");
            }
        }

        turno.rellamarDesdeHistorial(idPuesto, idSucursalPuesto, idUsuario);
        return turnoCommandRepository.actualizar(turno);
    }
}
