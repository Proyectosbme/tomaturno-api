package com.empresa.tomaturno.framework.adapters.event;

import com.empresa.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.Estado;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioEventHandlerAdapter {

    private final UsuarioCommandInputPort usuarioCommandInputPort;

    public UsuarioEventHandlerAdapter(UsuarioCommandInputPort usuarioCommandInputPort) {
        this.usuarioCommandInputPort = usuarioCommandInputPort;
    }

    @Blocking
    @Transactional
    @ConsumeEvent("sucursal.creada")
    public void onSucursalCreada(SucursalCreadaEvent event) {
        Long idSucursal = event.getSucursalId();
        crearUsuario(idSucursal, "publico", "publico", "PUBLICO", "Usuario", "Publico");
        crearUsuario(idSucursal, "monitor", "monitor", "MONITOR", "Usuario", "Monitor");
    }

    private void crearUsuario(Long idSucursal, String codigo, String contrasena,
                              String perfil, String nombres, String apellidos) {
        Usuario usuario = new Usuario();
        usuario.setIdSucursal(idSucursal);
        usuario.setCodigoUsuario(codigo);
        usuario.setContrasena(contrasena);
        usuario.setPerfil(perfil);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setEstado(Estado.ACTIVO);
        usuarioCommandInputPort.crear(usuario, "sistema");
    }
}
