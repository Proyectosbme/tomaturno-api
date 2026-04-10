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
        Cola cola = obtenerCola(idSucursal, idCola);
        Detalle detalle = validarYObtenerDetalle(cola, idDetalle);
        String codigoBase = obtenerCodigoBase(cola, detalle);
        String codigoTurno = generarCodigoTurno(idSucursal, codigoBase);
        Turno turno = construirTurno(idSucursal, idCola, detalle, codigoTurno, idPersona, tipoCasoEspecial);
        return guardarTurno(turno);
    }

    private Cola obtenerCola(Long idSucursal, Long idCola) {
        Cola cola = colaQueryRepository.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new TurnoValidationException(
                    "Cola con idCola=" + idCola + " e idSucursal=" + idSucursal + " no encontrada");
        }
        return cola;
    }

    private Detalle validarYObtenerDetalle(Cola cola, Long idDetalle) {
        if (cola.getDetalles() == null || cola.getDetalles().isEmpty()) {
            if (idDetalle != null) {
                throw new TurnoValidationException(
                        "La cola '" + cola.getNombre() + "' no tiene detalles, no debe enviar idDetalle");
            }
            return null;
        }
        if (idDetalle == null) {
            throw new TurnoValidationException(
                    "La cola '" + cola.getNombre() + "' tiene detalles, debe seleccionar uno");
        }
        return cola.getDetalles().stream()
                .filter(d -> d.getCorrelativo().equals(idDetalle))
                .findFirst()
                .orElseThrow(() -> new TurnoValidationException(
                        "Detalle con id=" + idDetalle + " no encontrado en la cola"));
    }

    private String obtenerCodigoBase(Cola cola, Detalle detalle) {
        return detalle != null ? detalle.getCodigo() : cola.getCodigo();
    }

    private String generarCodigoTurno(Long idSucursal, String codigoBase) {
        Long numero = turnoQueryRepository.obtenerSiguienteNumero(idSucursal, LocalDate.now(), codigoBase);
        return codigoBase + "-" + String.format("%03d", numero);
    }

    private Turno construirTurno(Long idSucursal, Long idCola, Detalle detalle,
            String codigoTurno, Long idPersona, Integer tipoCasoEspecial) {
        Long idDetalleValido = detalle != null ? detalle.getCorrelativo() : null;
        Turno turno = Turno.inicializar(idSucursal, idCola, idDetalleValido, codigoTurno, idPersona, tipoCasoEspecial);
        turno.asignarId(turnoQueryRepository.obtenerSiguienteId());
        return turno;
    }

    private Turno guardarTurno(Turno turno) {
        return turnoCommandRepository.save(turno);
    }
}
