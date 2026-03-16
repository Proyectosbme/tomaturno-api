package com.empresa.tomaturno.framework.adapters.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntityPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.UsuarioJpaRepository;

@ApplicationScoped
public class AdminStartupBean {

    private static final String USUARIO_SISTEMA = "sistema";

    
    private final SucursalJpaRepository sucursalJpaRepository;

    private final UsuarioJpaRepository usuarioJpaRepository;

    private final ConfiguracionDefaultBean configuracionDefaultBean;

    @Inject
    public AdminStartupBean(SucursalJpaRepository sucursalJpaRepository, UsuarioJpaRepository usuarioJpaRepository, ConfiguracionDefaultBean configuracionDefaultBean) {
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.configuracionDefaultBean = configuracionDefaultBean;
    }

    @Transactional
    void onStart(@Observes StartupEvent event) {
        SucursalJpaEntity sucursal = obtenerOCrearSucursalDefault();
        crearAdminSiNoExiste(sucursal.getId());
        crearConfiguracionesDefault();
    }

    private SucursalJpaEntity obtenerOCrearSucursalDefault() {
        List<SucursalJpaEntity> sucursales = sucursalJpaRepository.listAll();
        if (!sucursales.isEmpty()) {
            return sucursales.get(0);
        }
        SucursalJpaEntity nueva = new SucursalJpaEntity();
        nueva.setNombre("Sede Central");
        nueva.setEstado(1);
        nueva.setUsuarioCreacion(USUARIO_SISTEMA);
        nueva.setFechaCreacion(LocalDateTime.now());
        sucursalJpaRepository.persist(nueva);
        return nueva;
    }

    private void crearAdminSiNoExiste(Long idSucursal) {
        boolean adminExiste = usuarioJpaRepository
                .count("upper(codigoUsuario) = 'ADMIN'") > 0;
        if (adminExiste) return;

        Long nextId = usuarioJpaRepository.obtenerSiguienteId(idSucursal);
        UsuarioJpaEntityPK pk = new UsuarioJpaEntityPK(nextId, idSucursal);

        UsuarioJpaEntity admin = new UsuarioJpaEntity();
        admin.setIdpk(pk);
        admin.setCodigoUsuario("admin");
        admin.setContrasena(BCrypt.withDefaults().hashToString(12, "admin".toCharArray()));
        admin.setPerfil("ADMIN");
        admin.setNombres("Administrador");
        admin.setApellidos("Sistema");
        admin.setEstado(1);
        admin.setFechaCreacion(LocalDateTime.now());
        admin.setUserCreacion(USUARIO_SISTEMA);
        usuarioJpaRepository.persist(admin);
    }

    private void crearConfiguracionesDefault() {
        List<SucursalJpaEntity> sucursales = sucursalJpaRepository.listAll();
        for (SucursalJpaEntity sucursal : sucursales) {
            configuracionDefaultBean.crearConfiguracionesParaSucursal(sucursal.getId());
        }
    }
}
