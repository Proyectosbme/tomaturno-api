package com.empresa.tomaturno.framework.adapters.config;

import com.empresa.tomaturno.cola.application.command.port.input.ColaCommandInputPort;
import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.service.ColaCommandService;
import com.empresa.tomaturno.cola.application.query.port.input.ColaQueryInputPort;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.application.query.service.ColaQueryService;
import com.empresa.tomaturno.configuracion.application.command.port.input.ConfiguracionCommandInputPort;
import com.empresa.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.empresa.tomaturno.configuracion.application.command.service.ConfiguracionCommandService;
import com.empresa.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.empresa.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.empresa.tomaturno.configuracion.application.query.service.ConfiguracionQueryService;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.input.DetalleColaxPuestoCommandInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.command.service.DetalleColaxPuestoCommandService;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.input.DetalleColaxPuestoQueryInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.service.DetalleColaxPuestoQueryService;
import com.empresa.tomaturno.persona.application.command.port.input.PersonaCommandInputPort;
import com.empresa.tomaturno.persona.application.command.port.output.PersonaCommandRepository;
import com.empresa.tomaturno.persona.application.command.service.PersonaCommandService;
import com.empresa.tomaturno.persona.application.query.port.output.PersonaQueryRepository;
import com.empresa.tomaturno.puesto.application.command.port.input.PuestoCommandInputPort;
import com.empresa.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.empresa.tomaturno.puesto.application.command.service.PuestoCommandService;
import com.empresa.tomaturno.puesto.application.query.port.input.PuestoQueryInputPort;
import com.empresa.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.empresa.tomaturno.puesto.application.query.service.PuestoQueryService;
import com.empresa.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;
import com.empresa.tomaturno.sucursal.application.command.service.SucursalCommandService;
import com.empresa.tomaturno.sucursal.application.query.port.input.SucursalQueryInputPort;
import com.empresa.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.empresa.tomaturno.sucursal.application.query.service.SucursalQueryService;
import com.empresa.tomaturno.turno.application.command.port.input.TurnoCommandInputPort;
import com.empresa.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.empresa.tomaturno.turno.application.command.service.TurnoCommandService;
import com.empresa.tomaturno.turno.application.query.port.input.TurnoQueryInputPort;
import com.empresa.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.empresa.tomaturno.turno.application.query.service.TurnoQueryService;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.empresa.tomaturno.usuario.application.command.service.UsuarioCommandService;
import com.empresa.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.empresa.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.empresa.tomaturno.usuario.application.query.service.UsuarioQueryService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ApplicationConfig {

    private final SucursalQueryRepository sucursalQueryRepository;
    private final SucursalCommandRepository sucursalCommandRepository;
    private final ColaCommandRepository colaCommandRepository;
    private final ColaQueryRepository colaQueryRepository;
    private final PuestoCommandRepository puestoCommandRepository;
    private final PuestoQueryRepository puestoQueryRepository;
    private final DetalleColaxPuestoCommandRepository detalleColaxPuestoCommandRepository;
    private final DetalleColaxPuestoQueryRepository detalleColaxPuestoQueryRepository;
    private final UsuarioCommandRepository usuarioCommandRepository;
    private final UsuarioQueryRepository usuarioQueryRepository;
    private final ConfiguracionCommandRepository configuracionCommandRepository;
    private final ConfiguracionQueryRepository configuracionQueryRepository;
    private final TurnoCommandRepository turnoCommandRepository;
    private final TurnoQueryRepository turnoQueryRepository;
    private final PersonaCommandRepository personaCommandRepository;
    private final PersonaQueryRepository personaQueryRepository;

    public ApplicationConfig(SucursalQueryRepository sucursalQueryRepository,
                             SucursalCommandRepository sucursalCommandRepository,
                             ColaCommandRepository colaCommandRepository,
                             ColaQueryRepository colaQueryRepository,
                             PuestoCommandRepository puestoCommandRepository,
                             PuestoQueryRepository puestoQueryRepository,
                             DetalleColaxPuestoCommandRepository detalleColaxPuestoCommandRepository,
                             DetalleColaxPuestoQueryRepository detalleColaxPuestoQueryRepository,
                             UsuarioCommandRepository usuarioCommandRepository,
                             UsuarioQueryRepository usuarioQueryRepository,
                             ConfiguracionCommandRepository configuracionCommandRepository,
                             ConfiguracionQueryRepository configuracionQueryRepository,
                             TurnoCommandRepository turnoCommandRepository,
                             TurnoQueryRepository turnoQueryRepository,
                             PersonaCommandRepository personaCommandRepository,
                             PersonaQueryRepository personaQueryRepository) {
        this.sucursalQueryRepository = sucursalQueryRepository;
        this.sucursalCommandRepository = sucursalCommandRepository;
        this.colaCommandRepository = colaCommandRepository;
        this.colaQueryRepository = colaQueryRepository;
        this.puestoCommandRepository = puestoCommandRepository;
        this.puestoQueryRepository = puestoQueryRepository;
        this.detalleColaxPuestoCommandRepository = detalleColaxPuestoCommandRepository;
        this.detalleColaxPuestoQueryRepository = detalleColaxPuestoQueryRepository;
        this.usuarioCommandRepository = usuarioCommandRepository;
        this.usuarioQueryRepository = usuarioQueryRepository;
        this.configuracionCommandRepository = configuracionCommandRepository;
        this.configuracionQueryRepository = configuracionQueryRepository;
        this.turnoCommandRepository = turnoCommandRepository;
        this.turnoQueryRepository = turnoQueryRepository;
        this.personaCommandRepository = personaCommandRepository;
        this.personaQueryRepository = personaQueryRepository;
    }


    @Produces
    @ApplicationScoped
    public PersonaCommandInputPort personaCommandService() {
        return new PersonaCommandService(personaCommandRepository, personaQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public ColaCommandInputPort colaCommandService() {
        return new ColaCommandService(colaCommandRepository, colaQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public ColaQueryInputPort colaQueryService() {
        return new ColaQueryService(colaQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public PuestoCommandInputPort puestoCommandService() {
        return new PuestoCommandService(puestoCommandRepository, puestoQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public PuestoQueryInputPort puestoQueryService() {
        return new PuestoQueryService(puestoQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public DetalleColaxPuestoCommandInputPort detalleColaxPuestoCommandService() {
        return new DetalleColaxPuestoCommandService(detalleColaxPuestoCommandRepository, detalleColaxPuestoQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public DetalleColaxPuestoQueryInputPort detalleColaxPuestoQueryService() {
        return new DetalleColaxPuestoQueryService(detalleColaxPuestoQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public UsuarioCommandInputPort usuarioCommandService() {
        return new UsuarioCommandService(usuarioCommandRepository, usuarioQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public UsuarioQueryInputPort usuarioQueryService() {
        return new UsuarioQueryService(usuarioQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public ConfiguracionCommandInputPort configuracionCommandService() {
        return new ConfiguracionCommandService(configuracionCommandRepository, configuracionQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public ConfiguracionQueryInputPort configuracionQueryService() {
        return new ConfiguracionQueryService(configuracionQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public TurnoCommandInputPort turnoCommandService() {
        return new TurnoCommandService(turnoCommandRepository, turnoQueryRepository, colaQueryRepository, configuracionQueryRepository);
    }

    @Produces
    @ApplicationScoped
    public TurnoQueryInputPort turnoQueryService() {
        return new TurnoQueryService(turnoQueryRepository);
    }


    @Produces
    @ApplicationScoped
    public SucursalCommandInputPort sucursalCommandService(SucursalEventPublisher sucursalEventPublisher) {
        return new SucursalCommandService(sucursalCommandRepository, sucursalQueryRepository, sucursalEventPublisher);
    }

    @Produces
    @ApplicationScoped
    public SucursalQueryInputPort sucursalQueryService() {
        return new SucursalQueryService(sucursalQueryRepository);
    }
}
