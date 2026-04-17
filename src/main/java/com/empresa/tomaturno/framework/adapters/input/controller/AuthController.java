package com.empresa.tomaturno.framework.adapters.input.controller;

import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.UsuarioInputMapper;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

import io.quarkus.security.Authenticated;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/auth")
public class AuthController {

    private final UsuarioQueryInputPort queryPort;
    private final UsuarioInputMapper mapper;
    private final UsuarioCommandInputPort commandPort;

    @Context
    SecurityContext securityContext;

    public AuthController(UsuarioQueryInputPort queryPort, UsuarioInputMapper mapper,
            UsuarioCommandInputPort commandPort) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.mapper = mapper;
    }

    /**
     * Devuelve el perfil del usuario autenticado leyendo su codigoUsuario
     * desde el JWT (preferred_username de Keycloak).
     */
    @GET
    @Path("/perfil")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response perfil() {
        if (securityContext.getUserPrincipal() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String username = securityContext.getUserPrincipal().getName();
        Usuario usuario = queryPort.buscarPorCodigo(username);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UsuarioResponseDTO dto = mapper.toResponse(usuario);
        return Response.ok(dto).build();
    }

    @POST
    @Path("/crear")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registro(@Valid UsuarioRequestDTO dto) {
        Usuario usuario = mapper.toDomain(dto);
        usuario = commandPort.registro(usuario);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(usuario)).build();
    }
}
