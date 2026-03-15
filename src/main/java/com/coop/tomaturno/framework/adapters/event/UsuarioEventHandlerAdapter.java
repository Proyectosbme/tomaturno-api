package com.coop.tomaturno.framework.adapters.event;

import com.coop.tomaturno.sucursal.dominio.event.SucursalCreadaEvent;
import com.coop.tomaturno.usuario.application.command.usecase.CrearUsuarioUseCase;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;
import com.coop.tomaturno.usuario.dominio.vo.Estado;
import io.quarkus.vertx.ConsumeEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioEventHandlerAdapter {

    private final CrearUsuarioUseCase crearUsuarioUseCase;

    @Inject
    public UsuarioEventHandlerAdapter(CrearUsuarioUseCase crearUsuarioUseCase) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
    }

    @ConsumeEvent("sucursal.creada")
    public void onSucursalCreada(SucursalCreadaEvent event) {
        Long idSucursal = event.getSucursalId();
        crearUsuario(idSucursal, "publico", "publico", "PUBLICO", "Usuario", "Publico");
        crearUsuario(idSucursal, "monitor", "monitor", "MONITOR", "Usuario", "Monitor");
    }

    private void crearUsuario(Long idSucursal, String codigo, String contrasena,
                              String perfil, String nombres, String apellidos) {
        Usuario usuario = new Usuario();
        usuario.setIdSucursal(idSucursal);
        usuario.setCodigoUsuario(codigo);
        usuario.setContrasena(contrasena);
        usuario.setPerfil(perfil);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setEstado(Estado.ACTIVO);
        crearUsuarioUseCase.ejecutar(usuario);
    }
}
