package com.empresa.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.empresa.tomaturno.detallecolaxpuesto.application.command.port.input.DetalleColaxPuestoCommandInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.application.query.port.input.DetalleColaxPuestoQueryInputPort;
import com.empresa.tomaturno.detallecolaxpuesto.dominio.entity.DetalleColaxPuesto;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleColaxPuestoResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.DetalleColaxPuestoInputMapper;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/detallecolaxpuesto")
@Authenticated
public class DetalleColaxPuestoController {

    private static final String USUARIO_DEFAULT = "sistema";

    private final DetalleColaxPuestoCommandInputPort commandPort;
    private final DetalleColaxPuestoQueryInputPort queryPort;
    private final DetalleColaxPuestoInputMapper mapper;

    @Context
    SecurityContext securityContext;

    public DetalleColaxPuestoController(DetalleColaxPuestoCommandInputPort commandPort,
                                         DetalleColaxPuestoQueryInputPort queryPort,
                                         DetalleColaxPuestoInputMapper mapper) {
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
    public List<DetalleColaxPuestoResponseDTO> listarPorPuesto(
            @QueryParam("idPuesto") Long idPuesto,
            @QueryParam("idSucursalPuesto") Long idSucursalPuesto) {
        List<DetalleColaxPuesto> asignaciones = queryPort.listarPorPuesto(idPuesto, idSucursalPuesto);
        return asignaciones.stream().map(mapper::toResponse).toList();
    }

    @POST
    @Path("/asignar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response asignar(@Valid DetalleColaxPuestoRequestDTO dto) {
        DetalleColaxPuesto domain = mapper.toDomain(dto);
        DetalleColaxPuesto resultado = commandPort.asignar(domain, usuarioActual());
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(resultado)).build();
    }

    @DELETE
    @Path("/{idPuesto}/{idSucursalPuesto}/{idCola}/{idDetalle}/{idSucursalCola}")
    @Transactional
    @RolesAllowed({"ADMIN"})
    public Response desasignar(
            @PathParam("idPuesto") Long idPuesto,
            @PathParam("idSucursalPuesto") Long idSucursalPuesto,
            @PathParam("idCola") Long idCola,
            @PathParam("idDetalle") Long idDetalle,
            @PathParam("idSucursalCola") Long idSucursalCola) {
        commandPort.desasignar(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
        return Response.noContent().build();
    }
}
