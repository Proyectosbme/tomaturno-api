package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioRegistroRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioResponseDTO;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

@Mapper(componentModel = "cdi")
public interface UsuarioInputMapper {

    /**
     * Builder parcial: sin código de usuario ni auditoría de creación todavía — los
     * resuelve el caso de uso (CrearUsuarioUseCase/ModificarUsuarioUseCase) antes de
     * llamar a Usuario.of.
     */
    default Usuario.Builder toBuilder(UsuarioRequestDTO dto) {
        DatosPersonales datos = DatosPersonales.crear(
                dto.getNombres(), dto.getApellidos(), dto.getDui(), dto.getTelefono(), dto.getCorreo());
        ConfiguracionOperador config = ConfiguracionOperador.crear(
                dto.getPerfil(), dto.getIp(), dto.getCorrelativo(), dto.getAtenderCasosEspeciales());
        Usuario.Builder builder = new Usuario.Builder()
                .idSucursal(dto.getIdSucursal())
                .idPuesto(dto.getIdPuesto())
                .estado(dto.getEstado() != null ? Estado.fromCodigo(dto.getEstado()) : null)
                .datosPersonales(datos)
                .configuracion(config);
        if (dto.getPerfilCreador() != null && !dto.getPerfilCreador().isBlank())
            builder.perfilCreador(dto.getPerfilCreador());
        return builder;
    }

    default Usuario.Builder toRegistrarBuilder(UsuarioRegistroRequestDTO dto) {
        DatosPersonales datos = DatosPersonales.crear(
                dto.getNombres(), dto.getApellidos(), dto.getDui(), dto.getTelefono(), dto.getCorreo());
        ConfiguracionOperador config = ConfiguracionOperador.crear(
                dto.getPerfil(), null, dto.getCorrelativo(), null);
        return new Usuario.Builder()
                .idSucursal(dto.getIdSucursal())
                .idPuesto(dto.getIdPuesto())
                .estado(Estado.ACTIVO)
                .datosPersonales(datos)
                .configuracion(config);
    }

    @Mapping(source = "identificador",                          target = "id")
    @Mapping(source = "datosPersonales.nombres",                target = "nombres")
    @Mapping(source = "datosPersonales.apellidos",              target = "apellidos")
    @Mapping(source = "datosPersonales.dui",                    target = "dui")
    @Mapping(source = "datosPersonales.telefono",               target = "telefono")
    @Mapping(source = "configuracion.perfil",                   target = "perfil")
    @Mapping(source = "datosPersonales.correo",                 target = "correo")
    @Mapping(source = "configuracion.ip",                       target = "ip")
    @Mapping(source = "configuracion.correlativoPuesto",              target = "correlativo")
    @Mapping(source = "configuracion.atenderCasosEspeciales",   target = "atenderCasosEspeciales")
    @Mapping(source = "nombreSucursal",                         target = "nombreSucursal")
    @Mapping(source = "nombrePuesto",                           target = "nombrePuesto")
    @Mapping(source = "auditoriaCreacion.usuario",              target = "usuarioCreacion")
    @Mapping(source = "auditoriaCreacion.fecha",                target = "fechaCreacion")
    @Mapping(source = "auditoriaModificacion.usuario",          target = "usuarioModificacion")
    @Mapping(source = "auditoriaModificacion.fecha",            target = "fechaModificacion")
    @Mapping(source = "estado",                                 target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "foto",                                   target = "foto")
    UsuarioResponseDTO toResponse(Usuario usuario);

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
