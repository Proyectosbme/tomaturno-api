package com.empresa.tomaturno.framework.adapters.config;

import io.quarkus.arc.Arc;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

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

    private ConfiguracionDefaultBean self() {
        return Arc.container().select(ConfiguracionDefaultBean.class).get();
    }

    public void crearConfiguracionesParaSucursal(Long idSucursal) {
        crearIgnorandoDuplicado(idSucursal, ConfiguracionClave.VALIDAR_IP.clave(),                 1, "Valida la IP del usuario al iniciar sesión,0=desactivado, 1=activado");
        crearIgnorandoDuplicado(idSucursal, ConfiguracionClave.REINICIAR_NUMERACION.clave(),       1, "Reinicia la numeración de turnos diariamente,0=desactivado, 1=activado");
        crearIgnorandoDuplicado(idSucursal, ConfiguracionClave.NUMERACION_POR_COLA_DETALLE.clave(),1, "Numeración independiente por cola-detalle,0=desactivado, 1=activado");
        crearIgnorandoDuplicado(idSucursal, ConfiguracionClave.LLAMAR_CON_ACTIVO.clave(),          0, "Permite al operador llamar otro turno teniendo uno activo. 0=no permite, 1=permite");
        crearIgnorandoDuplicado(idSucursal, ConfiguracionClave.ESCANEAR_DUI.clave(),               0, "Activa el lector de código de barras 2D para DUI. 0=desactivado, 1=activado");
        crearIgnorandoDuplicado(idSucursal, ConfiguracionClave.CASOS_ESPECIALES.clave(),           0, "Activa selección de casos especiales en kiosco. 0=desactivado, 1=activado");
    }

    private void crearIgnorandoDuplicado(Long idSucursal, String nombre, Integer parametro, String descripcion) {
        try {
            self().crearSiNoExiste(idSucursal, nombre, parametro, descripcion);
        } catch (PersistenceException ignored) {
            // ya fue creado por una llamada concurrente — se ignora
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void crearSiNoExiste(Long idSucursal, String nombre, Integer parametro, String descripcion) {
        if (configuracionJpaRepository.buscarPorNombreYSucursal(idSucursal, nombre) != null) return;

        Long nextId = configuracionJpaRepository.obtenerSiguienteId(idSucursal);
        ConfiguracionJpaEntityPK pk = new ConfiguracionJpaEntityPK(nextId, idSucursal);

        ConfiguracionJpaEntity config = new ConfiguracionJpaEntity();
        config.setIdpk(pk);
        config.setNombre(nombre);
        config.setParametro(parametro);
        config.setValorTexto("");
        config.setDescripcion(descripcion);
        config.setEstado(1);
        config.setFechaCreacion(LocalDateTime.now());
        config.setUserCreacion(USUARIO_SISTEMA);
        configuracionJpaRepository.persist(config);
    }
}
