package com.coop.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.coop.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;
import com.coop.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.coop.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.coop.tomaturno.turno.dominio.entity.Turno;
import com.coop.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.coop.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class RellamarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;
    private final ConfiguracionQueryRepository configuracionQueryRepository;

    public RellamarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository,
            ConfiguracionQueryRepository configuracionQueryRepository) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
        this.configuracionQueryRepository = configuracionQueryRepository;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idPuesto, Long idSucursalPuesto, Long idUsuario) {

        Turno turno = turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (turno == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno);
        }

        // Si parametro=0, el operador no puede tener otro turno activo (LLAMADO)
        Configuracion config = configuracionQueryRepository.buscarPorNombreYSucursal(idSucursal, "LLAMAR_CON_ACTIVO");
        if (config != null && Integer.valueOf(0).equals(config.getParametro())) {
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
