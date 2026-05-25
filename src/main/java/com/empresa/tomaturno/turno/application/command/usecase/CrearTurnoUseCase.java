package com.empresa.tomaturno.turno.application.command.usecase;

import java.time.LocalDate;

import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
public class CrearTurnoUseCase {

    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;


    public CrearTurnoUseCase(TurnoCommandRepository turnoCommandRepository,
            TurnoQueryRepository turnoQueryRepository) {
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;

    }

    public Turno ejecutar(Long idSucursal, Long idCola, Long idDetalle, Long idPersona, Integer tipoCasoEspecial) {
        String codigoBase =turnoQueryRepository.obtenerCodigoBaseTurno(idSucursal, idCola, idDetalle);
        Long numero = turnoQueryRepository.obtenerSiguienteNumero(idSucursal, LocalDate.now(), codigoBase);
        String codigoTurno = Turno.generarCodigoTurno(codigoBase, numero);
        Long correlativo =  turnoQueryRepository.obtenerCorreltivoDetalle (idSucursal, idCola, idDetalle );

        Turno turno = Turno.inicializar(
                idSucursal, idCola, correlativo,
                codigoTurno, idPersona, tipoCasoEspecial);
                
        turno.asignarId(turnoQueryRepository.obtenerSiguienteId());
        return turnoCommandRepository.save(turno);
    }


}
