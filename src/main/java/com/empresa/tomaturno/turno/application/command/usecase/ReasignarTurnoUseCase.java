package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;

public class ReasignarTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;
    private final ColaQueryRepository colaQueryRepository;

    public ReasignarTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository,
            ColaQueryRepository colaQueryRepository) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public Turno ejecutar(Long idSucursal, LocalDateTime fechaCreacion, String codigoTurno,
            Long idSucursalDestino, Long idColaDestino, Long idDetalleDestino) {
        // 1. Obtener y validar turno original
        Turno original = turnoQueryRepository.buscarPorPK(idSucursal, fechaCreacion, codigoTurno);
        if (original == null) {
            throw new TurnoNotFoundException("Turno no encontrado: " + codigoTurno);
        }

        // 2. Validar cola destino y determinar idDetalle válido
        Cola cola = colaQueryRepository.buscarConDetallesPorIdYSucursal(idColaDestino, idSucursalDestino);
        if (cola == null) {
            throw new TurnoValidationException("Cola destino no encontrada: idCola=" + idColaDestino);
        }
        boolean colaConDetalles = cola.getDetalles() != null && !cola.getDetalles().isEmpty();
        Long idDetalleValido = null;

        if (colaConDetalles) {
            if (idDetalleDestino == null) {
                throw new TurnoValidationException("La cola destino tiene detalles, debe seleccionar uno");
            }
            cola.getDetalles().stream()
                    .filter(d -> d.getCorrelativo().equals(idDetalleDestino))
                    .findFirst()
                    .orElseThrow(() -> new TurnoValidationException("Detalle destino no encontrado en la cola"));
            idDetalleValido = idDetalleDestino;
        }

        // 3. Reasignar: valida transición, marca original como TRASLADO y construye el nuevo turno
        Turno nuevo = original.reasignarA(
                turnoQueryRepository.obtenerSiguienteId(),
                idSucursalDestino, idColaDestino, idDetalleValido);

        return turnoCommandRepository.reasignar(original, nuevo);
    }
}
