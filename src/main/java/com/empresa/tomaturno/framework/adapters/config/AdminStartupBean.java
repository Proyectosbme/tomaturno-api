package com.empresa.tomaturno.framework.adapters.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.EmpresaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.EmpresaJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.SucursalJpaRepository;
import com.empresa.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;
import com.empresa.tomaturno.shared.clases.Estado;

@ApplicationScoped
public class AdminStartupBean {

    private static final String USUARIO_SISTEMA = "sistema";

    private final SucursalJpaRepository sucursalJpaRepository;
    private final SucursalCommandInputPort sucursalCommandInputPort;
    private final EmpresaJpaRepository empresaJpaRepository;

    @Inject
    public AdminStartupBean(SucursalJpaRepository sucursalJpaRepository,
                            SucursalCommandInputPort sucursalCommandInputPort,
                            EmpresaJpaRepository empresaJpaRepository) {
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.sucursalCommandInputPort = sucursalCommandInputPort;
        this.empresaJpaRepository = empresaJpaRepository;
    }

    @Transactional
    void onStart(@Observes StartupEvent event) {
        obtenerOCrearSucursalDefault();
        crearEmpresaSiNoExiste();
    }

    private void obtenerOCrearSucursalDefault() {
        if (sucursalJpaRepository.count() > 0) return;

        Sucursal sucursal = Sucursal.inicializar("Administracion Central",
                Contacto.crear("00000000", "admin@sistema.com", "Sin direccion"), Estado.ACTIVO);
        sucursal.crear(USUARIO_SISTEMA);
        sucursalCommandInputPort.crear(sucursal, USUARIO_SISTEMA);
    }

    private void crearEmpresaSiNoExiste() {
        if (empresaJpaRepository.findById(1L) != null) return;

        EmpresaJpaEntity empresa = new EmpresaJpaEntity();
        empresa.setId(1L);
        empresa.setNombre("Mi Empresa");
        empresa.setBanner(null);
        empresa.setLogo(null);
        empresaJpaRepository.persist(empresa);
    }
}
