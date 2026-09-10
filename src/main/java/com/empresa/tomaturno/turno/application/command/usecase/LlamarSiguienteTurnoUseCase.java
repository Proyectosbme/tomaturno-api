package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;
import java.util.List;

import com.empresa.tomaturno.turno.application.command.port.input.TurnoCommandInputPort;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoGatewayPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;

public class LlamarSiguienteTurnoUseCase {

    private final TurnoGatewayPort turnoGatewayPort;
    private final TurnoCommandInputPort turnoCommandInputPort;

    public LlamarSiguienteTurnoUseCase(TurnoGatewayPort turnoGatewayPort,
            TurnoCommandInputPort turnoCommandInputPort) {
        this.turnoGatewayPort = turnoGatewayPort;
        this.turnoCommandInputPort = turnoCommandInputPort;
    }

    public Turno ejecutar(Long idSucursal, Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        List<Turno> pendientes = turnoGatewayPort.buscarPorFiltro(
                idSucursal, null, null, 1, LocalDate.now(), idPuesto, idSucursalPuesto);

        if (pendientes.isEmpty()) {
            throw new TurnoNotFoundException("No hay turnos pendientes para atender");
        }

        Turno siguiente = pendientes.get(0);
        return turnoCommandInputPort.llamar(
                siguiente.getIdSucursal(),
                siguiente.getFechaCreacion(),
                siguiente.getCodigoTurno(),
                idPuesto, idSucursalPuesto, idUsuario);
    }
}
