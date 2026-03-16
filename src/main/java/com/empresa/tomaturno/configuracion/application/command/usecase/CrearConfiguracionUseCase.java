package com.coop.tomaturno.configuracion.application.command.usecase;

import com.coop.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.coop.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;
import com.coop.tomaturno.configuracion.dominio.vo.Auditoria;

import java.time.LocalDateTime;

public class CrearConfiguracionUseCase {

    private final ConfiguracionCommandRepository commandRepository;
    private final ConfiguracionQueryRepository queryRepository;

    public CrearConfiguracionUseCase(ConfiguracionCommandRepository commandRepository,
                                     ConfiguracionQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Configuracion ejecutar(Configuracion configuracion) {
        Long siguienteId = queryRepository.obtenerSiguienteId(configuracion.getIdSucursal());
        configuracion.setIdConfiguracion(siguienteId);

        configuracion.setAuditoria(new Auditoria());
        configuracion.auditoriaCreacion("bmarroquin", LocalDateTime.now());
        configuracion.validarCreacion();

        return commandRepository.save(configuracion);
    }
}
