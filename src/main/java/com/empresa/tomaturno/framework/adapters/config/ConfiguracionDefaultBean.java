package com.empresa.tomaturno.framework.adapters.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntityPK;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.ConfiguracionJpaRepository;
import org.hibernate.exception.ConstraintViolationException;

@ApplicationScoped
public class ConfiguracionDefaultBean {

    private static final String USUARIO_SISTEMA = "sistema";

    private final ConfiguracionJpaRepository configuracionJpaRepository;

    public ConfiguracionDefaultBean(ConfiguracionJpaRepository configuracionJpaRepository) {
        this.configuracionJpaRepository = configuracionJpaRepository;
    }

    public void crearConfiguracionesParaSucursal(Long idSucursal) {
        intentarCrear(idSucursal, "VALIDAR_IP",                 1, "Valida la IP del usuario al iniciar sesión,0=desactivado, 1=activado");
        intentarCrear(idSucursal, "REINICIAR_NUMERACION",       1, "Reinicia la numeración de turnos diariamente,0=desactivado, 1=activado");
        intentarCrear(idSucursal, "NUMERACION_POR_COLA_DETALLE",1, "Numeración independiente por cola-detalle,0=desactivado, 1=activado");
        intentarCrear(idSucursal, "LLAMAR_CON_ACTIVO",          0, "Permite al operador llamar otro turno teniendo uno activo. 0=no permite, 1=permite");
        intentarCrear(idSucursal, "ESCANEAR_DUI",               0, "Activa el lector de código de barras 2D para DUI. 0=desactivado, 1=activado");
        intentarCrear(idSucursal, "CASOS_ESPECIALES",           0, "Activa selección de casos especiales en kiosco. 0=desactivado, 1=activado");
    }

    private void intentarCrear(Long idSucursal, String nombre, Integer parametro, String descripcion) {
        try {
            crearSiNoExiste(idSucursal, nombre, parametro, descripcion);
        } catch (Exception e) {
            if (!causadaPorDuplicado(e)) {
                throw e instanceof RuntimeException runtimeException ? runtimeException : new RuntimeException(e);
            }
            // Ya existe por otra transacción concurrente, continuar
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void crearSiNoExiste(Long idSucursal, String nombre, Integer parametro, String descripcion) {
        ConfiguracionJpaEntity existente = configuracionJpaRepository.buscarPorNombreYSucursal(idSucursal, nombre);
        if (existente != null) return;

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

    private boolean causadaPorDuplicado(Throwable t) {
        while (t != null) {
            if (t instanceof ConstraintViolationException) return true;
            t = t.getCause();
        }
        return false;
    }
}
