package com.empresa.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.empresa.tomaturno.configuracion.application.command.port.input.ConfiguracionCommandInputPort;
import com.empresa.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.framework.adapters.input.dto.ConfiguracionRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ConfiguracionResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.ConfiguracionInputMapper;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/configuraciones")
@Authenticated
public class ConfiguracionController {

    private static final String USUARIO_DEFAULT = "sistema";

    private final ConfiguracionCommandInputPort commandPort;
    private final ConfiguracionQueryInputPort queryPort;
    private final ConfiguracionInputMapper mapper;

    @Context
    SecurityContext securityContext;

    public ConfiguracionController(ConfiguracionCommandInputPort commandPort,
                                   ConfiguracionQueryInputPort queryPort,
                                   ConfiguracionInputMapper mapper) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.mapper = mapper;
    }

    private String usuarioActual() {
        return securityContext != null && securityContext.getUserPrincipal() != null
                ? securityContext.getUserPrincipal().getName()
                : USUARIO_DEFAULT;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConfiguracionResponseDTO> buscarPorSucursal(
            @QueryParam("idSucursal") Long idSucursal) {
        List<Configuracion> configs = queryPort.buscarPorSucursal(idSucursal);
        return configs.stream().map(mapper::toResponse).toList();
    }

    @GET
    @Path("/{idConfiguracion}/sucursal/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("idConfiguracion") Long idConfiguracion,
            @PathParam("idSucursal") Long idSucursal) {
        Configuracion config = queryPort.buscarPorId(idConfiguracion, idSucursal);
        return Response.ok(mapper.toResponse(config)).build();
    }

    @POST
    @Path("/crear")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response crear(@Valid ConfiguracionRequestDTO dto) {
        Configuracion configuracion = mapper.toDomain(dto);
        configuracion = commandPort.crear(configuracion, usuarioActual());
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(configuracion)).build();
    }

    @PUT
    @Path("/{idConfiguracion}/sucursal/{idSucursal}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response modificar(
            @PathParam("idConfiguracion") Long idConfiguracion,
            @PathParam("idSucursal") Long idSucursal,
            @Valid ConfiguracionRequestDTO dto) {
        Configuracion datosNuevos = mapper.toDomain(dto);
        Configuracion actualizado = commandPort.actualizar(idConfiguracion, idSucursal, datosNuevos, usuarioActual());
        return Response.ok(mapper.toResponse(actualizado)).build();
    }
}
