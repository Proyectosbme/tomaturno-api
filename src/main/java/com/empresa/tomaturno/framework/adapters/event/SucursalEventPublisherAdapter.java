package com.empresa.tomaturno.framework.adapters.event;

import com.empresa.tomaturno.framework.adapters.config.ConfiguracionDefaultBean;
import com.empresa.tomaturno.sucursal.application.command.port.output.SucursalEventPublisher;
import com.empresa.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SucursalEventPublisherAdapter implements SucursalEventPublisher {

    private final ConfiguracionDefaultBean configuracionDefaultBean;
    private final UsuarioCommandInputPort usuarioCommandInputPort;

    public SucursalEventPublisherAdapter(ConfiguracionDefaultBean configuracionDefaultBean,
                                         UsuarioCommandInputPort usuarioCommandInputPort) {
        this.configuracionDefaultBean = configuracionDefaultBean;
        this.usuarioCommandInputPort = usuarioCommandInputPort;
    }

    @Override
    @Transactional
    public void publishSucursalCreada(SucursalCreadaEvent event) {
        Long idSucursal = event.getSucursalId();
        configuracionDefaultBean.crearConfiguracionesParaSucursal(idSucursal);
        crearUsuariosDeSucursal(idSucursal);
    }

    private void crearUsuariosDeSucursal(Long idSucursal) {
        crearUsuario(idSucursal, "publico", "PUBLICO", "Publico");
        crearUsuario(idSucursal, "monitor", "MONITOR", "Monitor");
        crearUsuario(idSucursal, "admin",   "ADMIN",   "Admin");
    }

    private void crearUsuario(Long idSucursal, String codigoBase, String perfil, String apellidos) {
        String codigo = codigoBase + "-" + idSucursal;
        DatosPersonales datos = DatosPersonales.crear("Usuario", apellidos, null, null);
        ConfiguracionOperador config = ConfiguracionOperador.crear(perfil, null, null, null);
        Usuario usuario = Usuario.inicializar(idSucursal, null, codigo, Estado.ACTIVO, datos, config);
        usuario.asignarPerfilCreador("ADMIN");
        usuarioCommandInputPort.crear(usuario, "sistema");
    }
}
