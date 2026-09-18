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
        // Ya viene ordenada por prioridad de cola/detalle (ASC) y luego por fechaCreacion (ASC),
        // ver TurnoJpaRepository.buscarPorFiltros: JOIN contra detallecolaxpuesto del puesto.
        List<Turno> pendientes = turnoGatewayPort.buscarPorFiltro(
                idSucursal, null, null, 1, LocalDate.now(), idPuesto, idSucursalPuesto);

        if (pendientes.isEmpty()) {
            throw new TurnoNotFoundException("No hay turnos pendientes para atender");
        }

        Turno siguiente = seleccionarSiguiente(pendientes, idSucursal, idUsuario);
        return turnoCommandInputPort.llamar(
                siguiente.getIdSucursal(),
                siguiente.getFechaCreacion(),
                siguiente.getCodigoTurno(),
                idPuesto, idSucursalPuesto, idUsuario);
    }

    /**
     * Si el operador tiene activada la bandera de atender casos especiales, prioriza el
     * primer turno de caso especial pendiente en su cola/detalle (respetando el orden de
     * prioridad+llegada ya aplicado en la lista); si no hay ninguno pendiente, o el operador
     * no atiende casos especiales, sigue el orden normal (el primero de la lista).
     */
    private Turno seleccionarSiguiente(List<Turno> pendientes, Long idSucursal, Long idUsuario) {
        if (turnoGatewayPort.atiendeCasosEspeciales(idUsuario, idSucursal)) {
            return pendientes.stream()
                    .filter(t -> t.getTipoCasoEspecial() != null && t.getTipoCasoEspecial() != 0)
                    .findFirst()
                    .orElse(pendientes.get(0));
        }
        return pendientes.get(0);
    }
}
