package com.empresa.tomaturno.configuracion.application.command.usecase;

import java.time.LocalDateTime;

import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.configuracion.dominio.vo.Auditoria;

public class CrearConfiguracionUseCase {

    private final ConfiguracionCommandRepository commandRepository;
    private final ConfiguracionQueryRepository queryRepository;

    public CrearConfiguracionUseCase(ConfiguracionCommandRepository commandRepository,
                                     ConfiguracionQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    public Configuracion ejecutar(Configuracion configuracion,String usuario) {
        Long siguienteId = queryRepository.obtenerSiguienteId(configuracion.getIdSucursal());
        configuracion.setIdConfiguracion(siguienteId);

        configuracion.setAuditoria(new Auditoria());
        configuracion.auditoriaCreacion(usuario, LocalDateTime.now());
        configuracion.validarCreacion();

        return commandRepository.save(configuracion);
    }
}
