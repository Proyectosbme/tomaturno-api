package com.empresa.tomaturno.framework.adapters.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioJpaRepository;
import com.empresa.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.application.command.port.input.UsuarioCommandInputPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;

@ApplicationScoped
public class AdminStartupBean {

    private static final String USUARIO_SISTEMA = "sistema";

    private final SucursalJpaRepository sucursalJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final SucursalCommandInputPort sucursalCommandInputPort;
    private final UsuarioCommandInputPort usuarioCommandInputPort;

    @Inject
    public AdminStartupBean(SucursalJpaRepository sucursalJpaRepository,
                            UsuarioJpaRepository usuarioJpaRepository,
                            SucursalCommandInputPort sucursalCommandInputPort,
                            UsuarioCommandInputPort usuarioCommandInputPort) {
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.sucursalCommandInputPort = sucursalCommandInputPort;
        this.usuarioCommandInputPort = usuarioCommandInputPort;
    }

    @Transactional
    void onStart(@Observes StartupEvent event) {
        Long idSucursal = obtenerOCrearSucursalDefault();
        crearAdminSiNoExiste(idSucursal);
    }

    private Long obtenerOCrearSucursalDefault() {
        if (sucursalJpaRepository.count() > 0) {
            return sucursalJpaRepository.listAll().get(0).getId();
        }

          Sucursal sucursal = Sucursal.inicializar( "Administracion Central",  Contacto.crear("00000000", "admin@sistema.com", "Sin direccion"), Estado.ACTIVO);
          sucursal.crear(USUARIO_SISTEMA);
        Sucursal nueva = sucursalCommandInputPort.crear(sucursal, USUARIO_SISTEMA);
        return nueva.getIdentificador();
    }

    private void crearAdminSiNoExiste(Long idSucursal) {
        if (usuarioJpaRepository.count("upper(codigoUsuario) = 'ADMIN'") > 0) return;

        Usuario admin = new Usuario();
        admin.setIdSucursal(idSucursal);
        admin.setCodigoUsuario("admin");
        admin.setContrasena("admin");
        admin.setPerfil("ADMIN");
        admin.setNombres("Administrador");
        admin.setApellidos("Sistema");
        admin.setEstado(com.empresa.tomaturno.usuario.dominio.vo.Estado.ACTIVO);
        usuarioCommandInputPort.crear(admin, USUARIO_SISTEMA);
    }
}
