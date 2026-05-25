package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoConfiguracionPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;
import com.empresa.tomaturno.turno.dominio.vo.DetalleEstado;

public class LlamarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;
    private final TurnoConfiguracionPort turnoConfiguracionPort;

    public LlamarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
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
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno + " sucursal=" + idSucursal);
        }

        // Re-anuncio: el turno ya está LLAMADO por este mismo operador — no bloquear
        boolean mismoOperador = idPuesto.equals(turno.getIdPuesto())
                && (idUsuario == null || idUsuario.equals(turno.getIdUsuario()));
        if (turno.getEstado() != null && turno.getEstado().detalle() == DetalleEstado.LLAMADO.getValor() && mismoOperador) {
            turno.rellamar();
            return turnoCommandRepository.actualizar(turno);
        }

        // Llamada nueva: verificar LLAMAR_CON_ACTIVO
        if (turnoConfiguracionPort.debeVerificarTurnoActivo(idSucursal)) {
            boolean tieneActivo = (idUsuario != null)
                    ? turnoQueryRepository.existeTurnoLlamadoPorUsuario(idUsuario, idSucursal, LocalDate.now())
                    : turnoQueryRepository.existeTurnoLlamadoPorPuesto(idPuesto, idSucursal, LocalDate.now());
            if (tieneActivo) {
                throw new TurnoValidationException("El operador ya tiene un turno activo. Finalícelo antes de llamar otro.");
            }
        }

        turno.llamar(idPuesto, idSucursalPuesto, idUsuario);
        return turnoCommandRepository.actualizar(turno);
    }
}