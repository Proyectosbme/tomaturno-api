package com.empresa.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.input.dto.PuestoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.PuestoResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.PuestoInputMapper;
import com.empresa.tomaturno.puesto.application.command.port.input.PuestoCommandInputPort;
import com.empresa.tomaturno.puesto.application.query.port.input.PuestoQueryInputPort;
import com.empresa.tomaturno.puesto.dominio.entity.Puesto;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/puestos")
public class PuestoController {

    private static final String USUARIO_DEFAULT = "sistema";

    private final PuestoCommandInputPort puestoCommandInputPort;
    private final PuestoQueryInputPort puestoQueryInputPort;
    private final PuestoInputMapper puestoInputMapper;

    @Context
    SecurityContext securityContext;

    public PuestoController(PuestoCommandInputPort puestoCommandInputPort,
            PuestoQueryInputPort puestoQueryInputPort,
            PuestoInputMapper puestoInputMapper) {
        this.puestoCommandInputPort = puestoCommandInputPort;
        this.puestoQueryInputPort = puestoQueryInputPort;
        this.puestoInputMapper = puestoInputMapper;
    }

    private String usuarioActual() {
        return securityContext != null && securityContext.getUserPrincipal() != null
                ? securityContext.getUserPrincipal().getName()
                : USUARIO_DEFAULT;
    }

    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PuestoResponseDTO> buscarPuestos(
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("nombre") String nombre) {
        List<Puesto> puestos = puestoQueryInputPort.buscarPorFiltro(idSucursal, nombre);
        return puestos.stream().map(puestoInputMapper::toResponse).toList();
    }

    @GET
    @Path("/{idPuesto}/sucursal/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("idPuesto") Long idPuesto,
            @PathParam("idSucursal") Long idSucursal) {
        Puesto puesto = puestoQueryInputPort.buscarPorId(idPuesto, idSucursal);
        return Response.ok(puestoInputMapper.toResponse(puesto)).build();
    }

    @POST
    @Path("/crear")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearPuesto(@Valid PuestoRequestDTO dto) {
        Puesto puesto = puestoInputMapper.toDomain(dto);
        puesto = puestoCommandInputPort.crear(puesto, usuarioActual());
        return Response.status(Response.Status.CREATED)
                .entity(puestoInputMapper.toResponse(puesto)).build();
    }

    @PUT
    @Path("/{idPuesto}/sucursal/{idSucursal}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificarPuesto(
            @PathParam("idPuesto") Long idPuesto,
            @PathParam("idSucursal") Long idSucursal,
            @Valid PuestoRequestDTO dto) {
        Puesto datosNuevos = puestoInputMapper.toDomain(dto);
        Puesto puestoModificado = puestoCommandInputPort.actualizar(idPuesto, idSucursal, datosNuevos, usuarioActual());
        return Response.ok(puestoInputMapper.toResponse(puestoModificado)).build();
    }
}
