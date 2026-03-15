package com.coop.tomaturno.framework.adapters.event;

import com.coop.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;
import com.coop.tomaturno.configuracion.application.command.usecase.CrearConfiguracionUseCase;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;
import com.coop.tomaturno.configuracion.dominio.vo.Estado;
import io.quarkus.vertx.ConsumeEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConfiguracionEventHandlerAdapter {

    private final CrearConfiguracionUseCase crearConfiguracionUseCase;

    @Inject
    public ConfiguracionEventHandlerAdapter(CrearConfiguracionUseCase crearConfiguracionUseCase) {
        this.crearConfiguracionUseCase = crearConfiguracionUseCase;
    }

    @ConsumeEvent("sucursal.creada")
    public void onSucursalCreada(SucursalCreadaEvent event) {
        Long idSucursal = event.getSucursalId();
        crearConfiguracion(idSucursal, "VALIDAR_IP", 0, "Valida la IP del usuario al iniciar sesión");
        crearConfiguracion(idSucursal, "REINICIAR_NUMERACION", 1, "Reinicia la numeración de turnos diariamente");
        crearConfiguracion(idSucursal, "NUMERACION_POR_COLA_DETALLE", 1, "Numeración independiente por cola-detalle");
        crearConfiguracion(idSucursal, "LLAMAR_CON_ACTIVO", 0, "Permite al operador llamar otro turno teniendo uno activo");
    }

    private void crearConfiguracion(Long idSucursal, String nombre, int parametro, String descripcion) {
        Configuracion config = new Configuracion();
        config.setIdSucursal(idSucursal);
        config.setNombre(nombre);
        config.setParametro(parametro);
        config.setDescripcion(descripcion);
        config.setEstado(Estado.ACTIVO);
        crearConfiguracionUseCase.ejecutar(config);
    }
}
