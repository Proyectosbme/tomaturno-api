package com.coop.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.coop.tomaturno.framework.adapters.input.dto.UsuarioRequestDTO;
import com.coop.tomaturno.framework.adapters.input.dto.UsuarioResponseDTO;
import com.coop.tomaturno.framework.adapters.input.mapper.UsuarioInputMapper;
import com.coop.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.coop.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
public class UsuarioController {

    private final UsuarioCommandInputPort commandPort;
    private final UsuarioQueryInputPort queryPort;
    private final UsuarioInputMapper mapper;

    public UsuarioController(UsuarioCommandInputPort commandPort,
                              UsuarioQueryInputPort queryPort,
                              UsuarioInputMapper mapper) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.mapper = mapper;
    }

    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UsuarioResponseDTO> buscar(
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("codigoUsuario") String codigoUsuario,
            @QueryParam("nombre") String nombre) {
        List<Usuario> usuarios = queryPort.buscarPorFiltro(idSucursal, codigoUsuario, nombre);
        return usuarios.stream().map(mapper::toResponse).toList();
    }

    @GET
    @Path("/{idUsuario}/sucursal/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("idUsuario") Long idUsuario,
            @PathParam("idSucursal") Long idSucursal) {
        Usuario usuario = queryPort.buscarPorId(idUsuario, idSucursal);
        return Response.ok(mapper.toResponse(usuario)).build();
    }

    @POST
    @Path("/crear")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(@Valid UsuarioRequestDTO dto) {
        Usuario usuario = mapper.toDomain(dto);
        usuario = commandPort.crear(usuario);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(usuario)).build();
    }

    @PUT
    @Path("/{idUsuario}/sucursal/{idSucursal}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificar(
            @PathParam("idUsuario") Long idUsuario,
            @PathParam("idSucursal") Long idSucursal,
            @Valid UsuarioRequestDTO dto) {
        Usuario datosNuevos = mapper.toDomain(dto);
        Usuario actualizado = commandPort.actualizar(idUsuario, idSucursal, datosNuevos);
        return Response.ok(mapper.toResponse(actualizado)).build();
    }
}
