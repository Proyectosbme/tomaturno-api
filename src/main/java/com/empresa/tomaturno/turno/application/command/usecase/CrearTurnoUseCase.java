package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoGatewayPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
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
        Long correlativo =  turnoGatewayPort.obtenerCorreltivoDetalle (idSucursal, idCola, idDetalle );

        Turno turno = Turno.inicializar(
                idSucursal, idCola, correlativo,
                codigoTurno, idPersona, tipoCasoEspecial);

        turno.asignarId(turnoGatewayPort.obtenerSiguienteId());
        return turnoCommandRepository.save(turno);
    }


}
