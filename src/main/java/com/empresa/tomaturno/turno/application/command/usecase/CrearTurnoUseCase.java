package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;
import com.empresa.tomaturno.turno.dominio.vo.EstadoTurno;

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
        // 1. Obtener cola con detalles
        Cola cola = colaQueryRepository.buscarConDetallesPorIdYSucursal(idCola, idSucursal);
        if (cola == null) {
            throw new TurnoValidationException("Cola con idCola=" + idCola + " e idSucursal=" + idSucursal + " no encontrada");
        }

        // 2. Determinar codigoBase según regla de negocio
        boolean colaConDetalles = cola.getDetalles() != null && !cola.getDetalles().isEmpty();
        String codigoBase;
        Long idDetalleValido = null;

        if (colaConDetalles) {
            if (idDetalle == null) {
                throw new TurnoValidationException("La cola '" + cola.getNombre() + "' tiene detalles, debe seleccionar uno");
            }
            Detalle detalle = cola.getDetalles().stream()
                    .filter(d -> d.getCorrelativo().equals(idDetalle))
                    .findFirst()
                    .orElseThrow(() -> new TurnoValidationException("Detalle con id=" + idDetalle + " no encontrado en la cola"));
            codigoBase = detalle.getCodigo();
            idDetalleValido = idDetalle;
        } else {
            if (idDetalle != null) {
                throw new TurnoValidationException("La cola '" + cola.getNombre() + "' no tiene detalles, no debe enviar idDetalle");
            }
            codigoBase = cola.getCodigo();
        }

        // 3. Generar codigoTurno
        LocalDate hoy = LocalDate.now();
        Long numero = turnoQueryRepository.obtenerSiguienteNumero(idSucursal, hoy, codigoBase);
        String codigoTurno = codigoBase + "-" + String.format("%03d", numero);

        // 4. Construir entidad
        Turno turno = new Turno();
        turno.setIdSucursal(idSucursal);
        turno.setIdCola(idCola);
        turno.setIdDetalle(idDetalleValido);
        turno.setCodigoTurno(codigoTurno);
        turno.setFechaCreacion(LocalDateTime.now());
        turno.setEstado(EstadoTurno.CREADO);
        turno.setIdPersona(idPersona);
        turno.setTipoCasoEspecial(tipoCasoEspecial);

        turno.validarCreacion();

        // 5. Generar id de referencia
        turno.setId(turnoQueryRepository.obtenerSiguienteId());

        return turnoCommandRepository.save(turno);
    }
}
