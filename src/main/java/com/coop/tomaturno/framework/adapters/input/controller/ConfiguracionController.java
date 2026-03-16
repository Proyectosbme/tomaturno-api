package com.coop.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.coop.tomaturno.configuracion.application.command.port.input.ConfiguracionCommandInputPort;
import com.coop.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;
import com.coop.tomaturno.framework.adapters.input.dto.ConfiguracionRequestDTO;
import com.coop.tomaturno.framework.adapters.input.dto.ConfiguracionResponseDTO;
import com.coop.tomaturno.framework.adapters.input.mapper.ConfiguracionInputMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/configuraciones")
public class ConfiguracionController {

    private final ConfiguracionCommandInputPort commandPort;
    private final ConfiguracionQueryInputPort queryPort;
    private final ConfiguracionInputMapper mapper;

    public ConfiguracionController(ConfiguracionCommandInputPort commandPort,
                                   ConfiguracionQueryInputPort queryPort,
                                   ConfiguracionInputMapper mapper) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.mapper = mapper;
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
    public Response crear(@Valid ConfiguracionRequestDTO dto) {
        Configuracion configuracion = mapper.toDomain(dto);
        configuracion = commandPort.crear(configuracion);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(configuracion)).build();
    }

    @PUT
    @Path("/{idConfiguracion}/sucursal/{idSucursal}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificar(
            @PathParam("idConfiguracion") Long idConfiguracion,
            @PathParam("idSucursal") Long idSucursal,
            @Valid ConfiguracionRequestDTO dto) {
        Configuracion datosNuevos = mapper.toDomain(dto);
        Configuracion actualizado = commandPort.actualizar(idConfiguracion, idSucursal, datosNuevos);
        return Response.ok(mapper.toResponse(actualizado)).build();
    }
}
