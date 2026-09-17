package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoGatewayPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.exceptions.TurnoValidationException;
public class CrearTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoGatewayPort turnoGatewayPort;


    public CrearTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoGatewayPort turnoGatewayPort) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoGatewayPort = turnoGatewayPort;

    }

    public Turno ejecutar(Long idSucursal, Long idCola, Long idDetalle, Long idPersona, Integer tipoCasoEspecial) {
        String codigoBase =turnoGatewayPort.obtenerCodigoBaseTurno(idSucursal, idCola, idDetalle);
        Long numero = turnoGatewayPort.obtenerSiguienteNumero(idSucursal, LocalDate.now(), codigoBase);
        String codigoTurno = Turno.generarCodigoTurno(codigoBase, numero);

        // Valida que el detalle exista para esa cola/sucursal en vez de asumirlo: si no hace
        // match, el turno quedaría con idDetalle null y ningún operador podría llamarlo nunca
        // (el join de asignación por puesto exige idDetalle exacto).
        Long detalleValido = turnoGatewayPort.obtenerCorreltivoDetalle(idSucursal, idCola, idDetalle);
        if (detalleValido == null) {
            throw new TurnoValidationException("Detalle de cola no encontrado: idCola=" + idCola + " idDetalle=" + idDetalle);
        }

        Turno turno = Turno.inicializar(
                idSucursal, idCola, detalleValido,
                codigoTurno, idPersona, tipoCasoEspecial);

        turno.asignarId(turnoGatewayPort.obtenerSiguienteId());
        return turnoCommandRepository.save(turno);
    }


}
