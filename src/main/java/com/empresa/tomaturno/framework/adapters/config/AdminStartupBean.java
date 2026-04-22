package com.empresa.tomaturno.framework.adapters.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoDetalleJpaEntityPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.CatalogoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.EmpresaJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.CatalogoDetalleJpaRepository;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.CatalogoJpaRepository;
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
    private final CatalogoJpaRepository catalogoJpaRepository;
    private final CatalogoDetalleJpaRepository catalogoDetalleJpaRepository;

    @Inject
    public AdminStartupBean(SucursalJpaRepository sucursalJpaRepository,
                            SucursalCommandInputPort sucursalCommandInputPort,
                            EmpresaJpaRepository empresaJpaRepository,
                            CatalogoJpaRepository catalogoJpaRepository,
                            CatalogoDetalleJpaRepository catalogoDetalleJpaRepository) {
        this.sucursalJpaRepository = sucursalJpaRepository;
        this.sucursalCommandInputPort = sucursalCommandInputPort;
        this.empresaJpaRepository = empresaJpaRepository;
        this.catalogoJpaRepository = catalogoJpaRepository;
        this.catalogoDetalleJpaRepository = catalogoDetalleJpaRepository;
    }

    @Transactional
    void onStart(@Observes StartupEvent event) {
        obtenerOCrearSucursalDefault();
        crearEmpresaSiNoExiste();
        crearCatalogoEstadoTurnoSiNoExiste();
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

    private void crearCatalogoEstadoTurnoSiNoExiste() {
        if (catalogoJpaRepository.count("nombre", "ESTADO TURNO") > 0) return;

        CatalogoJpaEntity catalogo = new CatalogoJpaEntity();
        catalogo.setNombre("ESTADO TURNO");
        catalogo.setDescripcion("Estados del turno");
        catalogo.setUsuarioCreacion(USUARIO_SISTEMA);
        catalogo.setFechaCreacion(LocalDateTime.now());
        catalogo.setEstado(1);
        catalogoJpaRepository.persist(catalogo);
        catalogoJpaRepository.flush();

        Long idCatalogo = catalogo.getId();

        String[][] detalles = {
            {"1", "CREADO",      "Turno creado"},
            {"2", "LLAMADO",     "Turno llamado"},
            {"3", "TRASLADO",    "Turno trasladado"},
            {"4", "FINALIZADO",  "Turno finalizado"},
            {"5", "SIN_ATENDER", "Turno sin atender"}
        };

        for (String[] d : detalles) {
            CatalogoDetalleJpaEntity detalle = new CatalogoDetalleJpaEntity();
            detalle.setId(new CatalogoDetalleJpaEntityPK(idCatalogo, Long.parseLong(d[0])));
            detalle.setNombre(d[1]);
            detalle.setDescripcion(d[2]);
            detalle.setUsuarioCreacion(USUARIO_SISTEMA);
            detalle.setFechaCreacion(LocalDateTime.now());
            detalle.setEstado(1);
            catalogoDetalleJpaRepository.persist(detalle);
        }
    }
}
