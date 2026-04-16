package com.empresa.tomaturno.framework.adapters.input.filter;

import com.empresa.tomaturno.configuracion.application.query.port.input.ConfiguracionQueryInputPort;
import com.empresa.tomaturno.configuracion.dominio.entity.Configuracion;
import com.empresa.tomaturno.usuario.application.query.port.input.UsuarioQueryInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHORIZATION + 1)
public class IpValidationFilter implements ContainerRequestFilter {

    private final SecurityIdentity identity;
    private final UsuarioQueryInputPort usuarioQuery;
    private final ConfiguracionQueryInputPort configuracionQuery;



    
    public IpValidationFilter(SecurityIdentity identity, UsuarioQueryInputPort usuarioQuery,
            ConfiguracionQueryInputPort configuracionQuery) {
        this.identity = identity;
        this.usuarioQuery = usuarioQuery;
        this.configuracionQuery = configuracionQuery;
    }

    @Override
    public void filter(ContainerRequestContext ctx) {
        if (identity == null || identity.isAnonymous())
            return;

        String codigoUsuario = identity.getPrincipal().getName();
        Usuario usuario = usuarioQuery.buscarPorCodigo(codigoUsuario);
        if (usuario == null || usuario.getIdSucursal() == null)
            return;

        Configuracion config = configuracionQuery.buscarPorNombre(usuario.getIdSucursal(), "VALIDAR_IP");
        if (config == null || !Integer.valueOf(1).equals(config.getParametro()))
            return;

        String ipPermitida = usuario.getIp();
        if (ipPermitida == null || ipPermitida.isBlank())
            return;

        String ipCliente = obtenerIp(ctx);
        if (!ipPermitida.equals(ipCliente)) {
            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Acceso denegado: la IP " + ipCliente + " no está autorizada\"}")
                    .build());
        }
    }

    private String obtenerIp(ContainerRequestContext ctx) {
        String xRealIp = ctx.getHeaderString("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank())
            return xRealIp.trim();

        String xForwardedFor = ctx.getHeaderString("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return ctx.getUriInfo().getRequestUri().getHost();
    }
}
