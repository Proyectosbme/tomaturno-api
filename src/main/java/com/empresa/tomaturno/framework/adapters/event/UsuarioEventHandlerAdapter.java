package com.empresa.tomaturno.framework.adapters.event;

import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

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
        crearUsuario(idSucursal, "publico", "PUBLICO", "Usuario", "Publico");
        crearUsuario(idSucursal, "monitor", "MONITOR", "Usuario", "Monitor");
        crearUsuario(idSucursal, "admin", "ADMIN", "Usuario", "Admin");
    }

    private void crearUsuario(Long idSucursal, String codigoBase, String perfil,
            String nombres, String apellidos) {
        String codigo =  codigoBase+"-"+idSucursal;
        DatosPersonales datos = DatosPersonales.crear(nombres, apellidos, null, null);
        ConfiguracionOperador config = ConfiguracionOperador.crear(perfil, null, null, null);
        Usuario usuario = Usuario.inicializar(idSucursal, null, codigo, Estado.ACTIVO, datos, config);
        usuario.asignarPerfilCreador("ADMIN");
        usuarioCommandInputPort.crear(usuario, "sistema");
    }
}
