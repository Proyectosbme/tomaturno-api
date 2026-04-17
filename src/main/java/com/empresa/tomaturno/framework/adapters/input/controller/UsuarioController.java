package com.empresa.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.UsuarioInputMapper;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/usuarios")
public class UsuarioController {

    private static final String USUARIO_DEFAULT = "sistema";

    private final UsuarioCommandInputPort commandPort;
    private final UsuarioQueryInputPort queryPort;
    private final UsuarioInputMapper mapper;

    @Context
    SecurityContext securityContext;

    public UsuarioController(UsuarioCommandInputPort commandPort,
                              UsuarioQueryInputPort queryPort,
                              UsuarioInputMapper mapper) {
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
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public List<UsuarioResponseDTO> buscar(
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("codigoUsuario") String codigoUsuario) {
        List<Usuario> usuarios = queryPort.buscarPorFiltro(idSucursal, codigoUsuario);
        return usuarios.stream().map(mapper::toResponse).toList();
    }

    @GET
    @Path("/{idUsuario}/sucursal/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
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
        usuario = commandPort.crear(usuario, usuarioActual());
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
        Usuario actualizado = commandPort.actualizar(idUsuario, idSucursal, datosNuevos, usuarioActual());
        return Response.ok(mapper.toResponse(actualizado)).build();
    }

    @PATCH
    @Path("/{idUsuario}/sucursal/{idSucursal}/foto")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Authenticated
    public Response asignarFoto(@PathParam("idUsuario") Long idUsuario,
                                @PathParam("idSucursal") Long idSucursal,
                                @RestForm("foto") FileUpload foto) throws java.io.IOException {
        byte[] fotoBytes = java.nio.file.Files.readAllBytes(foto.uploadedFile());
        Usuario usuario = commandPort.asignarFoto(idUsuario, idSucursal, fotoBytes);
        return Response.ok(mapper.toResponse(usuario)).build();
    }

    @GET
    @Path("/{idUsuario}/sucursal/{idSucursal}/foto")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Authenticated
    public Response obtenerFoto(@PathParam("idUsuario") Long idUsuario,
                                @PathParam("idSucursal") Long idSucursal) {
        byte[] foto = queryPort.obtenerFoto(idUsuario, idSucursal);
        if (foto == null || foto.length == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(foto)
                .header("Content-Disposition", "attachment; filename=foto_" + idUsuario + ".jpg")
                .build();
    }
}
