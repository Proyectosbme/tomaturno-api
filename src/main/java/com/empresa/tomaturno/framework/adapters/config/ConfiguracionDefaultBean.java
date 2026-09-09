package com.empresa.tomaturno.framework.adapters.config;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntityPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ConfiguracionJpaRepository;
import com.empresa.tomaturno.shared.clases.ConfiguracionClave;

@ApplicationScoped
public class ConfiguracionDefaultBean {

    private static final String USUARIO_SISTEMA = "sistema";

    private final ConfiguracionJpaRepository configuracionJpaRepository;

    public ConfiguracionDefaultBean(ConfiguracionJpaRepository configuracionJpaRepository) {
        this.configuracionJpaRepository = configuracionJpaRepository;
    }

    public void crearConfiguracionesParaSucursal(Long idSucursal) {
        crearSiNoExiste(idSucursal, ConfiguracionClave.VALIDAR_IP.clave(),                 0, "Valida la IP del usuario al iniciar sesión,0=desactivado, 1=activado");
        crearSiNoExiste(idSucursal, ConfiguracionClave.LLAMAR_CON_ACTIVO.clave(),          0, "Permite al operador llamar otro turno teniendo uno activo. 0=no permite, 1=permite");
        crearSiNoExiste(idSucursal, ConfiguracionClave.ESCANEAR_DUI.clave(),               0, "Activa el lector de código de barras 2D para DUI. 0=desactivado, 1=activado");
        crearSiNoExiste(idSucursal, ConfiguracionClave.CASOS_ESPECIALES.clave(),           0, "Activa selección de casos especiales en kiosco. 0=desactivado, 1=activado");
        crearSiNoExiste(idSucursal, ConfiguracionClave.TURNO_AUTOMATICO.clave(),           0, "Llama automáticamente el siguiente turno en cola. 0=desactivado, 1=activado");
    }

    private void crearSiNoExiste(Long idSucursal, String nombre, Integer parametro, String descripcion) {
        if (configuracionJpaRepository.buscarPorNombreYSucursal(idSucursal, nombre) != null) return;

        Long nextId = configuracionJpaRepository.obtenerSiguienteId(idSucursal);
        ConfiguracionJpaEntityPK pk = new ConfiguracionJpaEntityPK(nextId, idSucursal);

        ConfiguracionJpaEntity config = new ConfiguracionJpaEntity();
        config.setIdpk(pk);
        config.setNombre(nombre);
        config.setParametro(parametro);
        config.setDescripcion(descripcion);
        config.setEstado(1);
        config.setFechaCreacion(LocalDateTime.now());
        config.setUserCreacion(USUARIO_SISTEMA);
        configuracionJpaRepository.persist(config);
    }
}
