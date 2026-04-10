package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class CrearTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;
    private final ColaQueryRepository colaQueryRepository;

    public CrearTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository,
            ColaQueryRepository colaQueryRepository) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public Turno ejecutar(Long idSucursal, Long idCola, Long idDetalle, Long idPersona, Integer tipoCasoEspecial) {
        Cola cola = colaQueryRepository.buscarPorIdColaYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new TurnoValidationException(
                    "Cola con idCola=" + idCola + " e idSucursal=" + idSucursal + " no encontrada");
        }

        Detalle detalle = idDetalle != null
                ? colaQueryRepository.obtenerDetalle(idCola, idSucursal, idDetalle)
                : null;
        String codigoBase = cola.resolverCodigoBase(detalle);
        Long numero = turnoQueryRepository.obtenerSiguienteNumero(idSucursal, LocalDate.now(), codigoBase);
        String codigoTurno = codigoBase + "-" + String.format("%03d", numero);

        Turno turno = Turno.inicializar(
                idSucursal, idCola, detalle != null ? detalle.getCorrelativo() : null,
                codigoTurno, idPersona, tipoCasoEspecial);
        turno.asignarId(turnoQueryRepository.obtenerSiguienteId());
        return turnoCommandRepository.save(turno);
    }
}
