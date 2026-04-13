package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;
import java.util.List;

import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.vo.EstadoTurno;

public class LlamarSiguienteTurnoUseCase {

    private final TurnoQueryRepository turnoQueryRepository;
    private final LlamarTurnoUseCase llamarTurnoUseCase;

    public LlamarSiguienteTurnoUseCase(TurnoQueryRepository turnoQueryRepository,
            LlamarTurnoUseCase llamarTurnoUseCase) {
        this.turnoQueryRepository = turnoQueryRepository;
        this.llamarTurnoUseCase = llamarTurnoUseCase;
    }

    public Turno ejecutar(Long idSucursal, Long idPuesto, Long idSucursalPuesto, Long idUsuario) {
        List<Turno> pendientes = turnoQueryRepository.buscarPorFiltro(
                idSucursal, null, null,
                EstadoTurno.CREADO.getCodigo(),
                LocalDate.now(),
                idPuesto, idSucursalPuesto);

        if (pendientes.isEmpty()) {
            throw new TurnoNotFoundException("No hay turnos pendientes para atender");
        }

        Turno siguiente = pendientes.get(0);
        return llamarTurnoUseCase.ejecutar(
                siguiente.getIdSucursal(),
                siguiente.getFechaCreacion(),
                siguiente.getCodigoTurno(),
                idPuesto, idSucursalPuesto, idUsuario);
    }
}
