package com.coop.tomaturno.framework.adapters.config;

import com.coop.tomaturno.cola.application.command.port.input.ColaCommandInputPort;
import com.coop.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.coop.tomaturno.cola.application.command.service.ColaCommandService;
import com.coop.tomaturno.cola.application.query.port.input.ColaQueryInputPort;
import com.coop.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.coop.tomaturno.cola.application.query.service.ColaQueryService;
import com.coop.tomaturno.configuracion.application.command.port.input.ConfiguracionCommandInputPort;
import com.coop.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.coop.tomaturno.configuracion.application.command.service.ConfiguracionCommandService;
import com.coop.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.coop.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.coop.tomaturno.configuracion.application.query.service.ConfiguracionQueryService;
import com.coop.tomaturno.detallecolaxpuesto.application.command.port.input.DetalleColaxPuestoCommandInputPort;
import com.coop.tomaturno.detallecolaxpuesto.application.command.port.output.DetalleColaxPuestoCommandRepository;
import com.coop.tomaturno.detallecolaxpuesto.application.command.service.DetalleColaxPuestoCommandService;
import com.coop.tomaturno.detallecolaxpuesto.application.query.port.input.DetalleColaxPuestoQueryInputPort;
import com.coop.tomaturno.detallecolaxpuesto.application.query.port.output.DetalleColaxPuestoQueryRepository;
import com.coop.tomaturno.detallecolaxpuesto.application.query.service.DetalleColaxPuestoQueryService;
import com.coop.tomaturno.puesto.application.command.port.input.PuestoCommandInputPort;
import com.coop.tomaturno.puesto.application.command.port.output.PuestoCommandRepository;
import com.coop.tomaturno.puesto.application.command.service.PuestoCommandService;
import com.coop.tomaturno.puesto.application.query.port.input.PuestoQueryInputPort;
import com.coop.tomaturno.puesto.application.query.port.output.PuestoQueryRepository;
import com.coop.tomaturno.puesto.application.query.service.PuestoQueryService;
import com.coop.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.coop.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.coop.tomaturno.sucursal.application.command.service.SucursalCommandService;
import com.coop.tomaturno.sucursal.application.query.port.input.SucursalQueryInputPort;
import com.coop.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.coop.tomaturno.sucursal.application.query.service.SucursalQueryService;
import com.coop.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.coop.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.coop.tomaturno.usuario.application.command.service.UsuarioCommandService;
import com.coop.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.application.query.service.UsuarioQueryService;
import com.coop.tomaturno.turno.application.command.port.input.TurnoCommandInputPort;
import com.coop.tomaturno.turno.application.command.port.output.TurnoCommandRepository;
import com.coop.tomaturno.turno.application.command.service.TurnoCommandService;
import com.coop.tomaturno.turno.application.query.port.input.TurnoQueryInputPort;
import com.coop.tomaturno.turno.application.query.port.output.TurnoQueryRepository;
import com.coop.tomaturno.turno.application.query.service.TurnoQueryService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import com.coop.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;
import com.coop.tomaturno.framework.adapters.event.SucursalEventPublisherAdapter;

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
                             TurnoQueryRepository turnoQueryRepository) {
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
    public SucursalEventPublisher sucursalEventPublisher(SucursalEventPublisherAdapter adapter) {
        return adapter;
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
