package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoConfiguracionPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class RellamarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;
    private final TurnoConfiguracionPort turnoConfiguracionPort;

    public RellamarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository,
            TurnoConfiguracionPort turnoConfiguracionPort) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
        this.turnoConfiguracionPort = turnoConfiguracionPort;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idPuesto, Long idSucursalPuesto, Long idUsuario) {

        Turno turno = turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (turno == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno);
        }

        if (idUsuario != null && !turnoConfiguracionPort.operadorActivo(idUsuario, idSucursal)) {
            throw new TurnoValidationException("No se puede llamar un turno: el operador no está activo. Actívate primero.");
        }

        if (turnoConfiguracionPort.debeVerificarTurnoActivo(idSucursal)) {
            boolean tieneActivo = (idUsuario != null)
                    ? turnoQueryRepository.existeTurnoLlamadoPorUsuario(idUsuario, idSucursal, LocalDate.now())
                    : turnoQueryRepository.existeTurnoLlamadoPorPuesto(idPuesto, idSucursal, LocalDate.now());
            if (tieneActivo) {
                throw new TurnoValidationException("El operador ya tiene un turno activo. Finalícelo antes de volver a llamar.");
            }
        }

        turno.rellamarDesdeHistorial(idPuesto, idSucursalPuesto, idUsuario);
        return turnoCommandRepository.actualizar(turno);
    }
}
