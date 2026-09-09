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
        String perfil = Long.valueOf(1L).equals(idSucursal) ? "ADMIN" : "SUBADMIN";

        crearUsuario(idSucursal, perfil, "Admin");
        crearUsuario(idSucursal, "PUBLICO", "Publico");
        crearUsuario(idSucursal, "MONITOR", "Monitor");
    }

    private void crearUsuario(Long idSucursal, String perfil, String apellidos) {
        // El código de usuario final lo deriva y desambigua CrearUsuarioUseCase a
        // partir de los datos personales (Usuario.generarCodigoUsuario), así que aquí
        // no hace falta proponer uno.
        DatosPersonales datos = DatosPersonales.crear("Usuario", apellidos, null, null, null);
        ConfiguracionOperador config = ConfiguracionOperador.crear(perfil, null, null, null);
        Usuario.Builder builder = new Usuario.Builder()
                .idSucursal(idSucursal)
                .estado(Estado.ACTIVO)
                .datosPersonales(datos)
                .configuracion(config)
                .perfilCreador("ADMIN");
        usuarioCommandInputPort.crear(builder, "sistema");
    }
}
