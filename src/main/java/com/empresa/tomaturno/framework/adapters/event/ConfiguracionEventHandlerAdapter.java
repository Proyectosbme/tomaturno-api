package com.empresa.tomaturno.framework.adapters.event;

import com.empresa.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;
import com.empresa.tomaturno.framework.adapters.config.ConfiguracionDefaultBean;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ConfiguracionEventHandlerAdapter {

    private final ConfiguracionDefaultBean configuracionDefaultBean;

    public ConfiguracionEventHandlerAdapter(ConfiguracionDefaultBean configuracionDefaultBean) {
        this.configuracionDefaultBean = configuracionDefaultBean;
    }

    @Blocking
    @Transactional
    @ConsumeEvent("sucursal.creada")
    public void onSucursalCreada(SucursalCreadaEvent event) {
        configuracionDefaultBean.crearConfiguracionesParaSucursal(event.getSucursalId());
    }
}
