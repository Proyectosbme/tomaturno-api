package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoGatewayPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;

public class MarcarEnEsperaUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoGatewayPort turnoGatewayPort;

    public MarcarEnEsperaUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoGatewayPort turnoGatewayPort) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoGatewayPort = turnoGatewayPort;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno) {
        Turno turno = turnoGatewayPort.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (turno == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno + " sucursal=" + idSucursal);
        }
        turno.enEspera();
        return turnoCommandRepository.actualizar(turno);
    }
}
