package com.empresa.tomaturno.framework.adapters.input.controller;

import com.empresa.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.framework.adapters.input.dto.LoginRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.UsuarioInputMapper;
import com.empresa.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.vertx.ext.web.RoutingContext;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    private final UsuarioQueryInputPort queryPort;
    private final UsuarioInputMapper mapper;
    private final ConfiguracionQueryInputPort configuracionQuery;
    private final RoutingContext routingContext;

    public AuthController(UsuarioQueryInputPort queryPort, UsuarioInputMapper mapper,
                          ConfiguracionQueryInputPort configuracionQuery,
                          RoutingContext routingContext) {
        this.queryPort = queryPort;
        this.mapper = mapper;
        this.configuracionQuery = configuracionQuery;
        this.routingContext = routingContext;
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO dto) {
        if (dto == null || dto.getCodigoUsuario() == null || dto.getContrasena() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Credenciales requeridas\"}")
                    .build();
        }

        Usuario usuario = dto.getIdSucursal() != null
                ? queryPort.buscarPorCodigoYSucursal(dto.getCodigoUsuario(), dto.getIdSucursal())
                : queryPort.buscarPorCodigo(dto.getCodigoUsuario());

        if (usuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Credenciales inválidas\"}")
                    .build();
        }

        BCrypt.Result result = BCrypt.verifyer().verify(
                dto.getContrasena().toCharArray(), usuario.getContrasena());

        if (!result.verified) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Credenciales inválidas\"}")
                    .build();
        }

        if (usuario.getIdSucursal() != null) {
            Configuracion validarIpConfig = configuracionQuery.buscarPorNombre(
                    usuario.getIdSucursal(), "VALIDAR_IP");
            if (validarIpConfig != null && Integer.valueOf(1).equals(validarIpConfig.getParametro())) {
                String ipCliente = obtenerIpCliente();
                String ipPermitida = usuario.getIp();
                if (ipPermitida != null && !ipPermitida.isBlank() && !ipPermitida.equals(ipCliente)) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("{\"error\":\"Acceso denegado: la IP " + ipCliente + " no está autorizada para este usuario\"}")
                            .build();
                }
            }
        }

        UsuarioResponseDTO response = mapper.toResponse(usuario);
        return Response.ok(response).build();
    }

    private String obtenerIpCliente() {
        String xRealIp = routingContext.request().getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) return xRealIp.trim();

        String xForwardedFor = routingContext.request().getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return routingContext.request().remoteAddress().host();
    }
}
