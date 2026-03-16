package com.coop.tomaturno.sucursal.application.command.service;

import com.coop.tomaturno.configuracion.application.command.port.output.ConfiguracionCommandRepository;
import com.coop.tomaturno.configuracion.application.command.usecase.CrearConfiguracionUseCase;
import com.coop.tomaturno.configuracion.application.query.port.output.ConfiguracionQueryRepository;
import com.coop.tomaturno.configuracion.dominio.entity.Configuracion;
import com.coop.tomaturno.configuracion.dominio.vo.Estado;
import com.coop.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.coop.tomaturno.sucursal.application.command.port.output.SucursalCommandRepository;
import com.coop.tomaturno.sucursal.application.command.usecase.CrearSucursalCaseUse;
import com.coop.tomaturno.sucursal.application.command.usecase.ModificarSucursalUseCase;
import com.coop.tomaturno.sucursal.application.query.port.output.SucursalQueryRepository;
import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;
import com.coop.tomaturno.usuario.application.command.port.output.UsuarioCommandRepository;
import com.coop.tomaturno.usuario.application.command.usecase.CrearUsuarioUseCase;
import com.coop.tomaturno.usuario.application.query.port.output.UsuarioQueryRepository;
import com.coop.tomaturno.usuario.dominio.entity.Usuario;

public class SucursalCommandService implements SucursalCommandInputPort {

    private final CrearSucursalCaseUse crearSucursalCaseUse;
    private final ModificarSucursalUseCase modificarSucursalUseCase;
    private final CrearConfiguracionUseCase crearConfiguracionUseCase;
    private final CrearUsuarioUseCase crearUsuarioUseCase;

    public SucursalCommandService(SucursalCommandRepository sucursalCommandRepository,
                                  SucursalQueryRepository sucursalQueryRepository,
                                  ConfiguracionCommandRepository configuracionCommandRepository,
                                  ConfiguracionQueryRepository configuracionQueryRepository,
                                  UsuarioCommandRepository usuarioCommandRepository,
                                  UsuarioQueryRepository usuarioQueryRepository) {
        this.crearSucursalCaseUse = new CrearSucursalCaseUse(sucursalCommandRepository);
        this.modificarSucursalUseCase = new ModificarSucursalUseCase(sucursalCommandRepository, sucursalQueryRepository);
        this.crearConfiguracionUseCase = new CrearConfiguracionUseCase(configuracionCommandRepository, configuracionQueryRepository);
        this.crearUsuarioUseCase = new CrearUsuarioUseCase(usuarioCommandRepository, usuarioQueryRepository);
    }

    @Override
    public Sucursal crear(Sucursal sucursal) {
        Sucursal creada = crearSucursalCaseUse.ejecutar(sucursal);
        crearConfiguracionesDefault(creada.getIdentificador());
        crearUsuariosDefault(creada.getIdentificador());
        return creada;
    }

    @Override
    public Sucursal actualizar(Long id, Sucursal datosActualizados) {
        return modificarSucursalUseCase.ejecutar(id, datosActualizados);
    }

    private void crearConfiguracionesDefault(Long idSucursal) {
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

    private void crearUsuariosDefault(Long idSucursal) {
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
        usuario.setEstado(com.coop.tomaturno.usuario.dominio.vo.Estado.ACTIVO);
        crearUsuarioUseCase.ejecutar(usuario);
    }
}
