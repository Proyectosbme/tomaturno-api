package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.UsuarioResponseDTO;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.ConfiguracionOperador;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

@Mapper(componentModel = "cdi")
public interface UsuarioInputMapper {

    default Usuario toDomain(UsuarioRequestDTO dto) {
        DatosPersonales datos = DatosPersonales.crear(
                dto.getNombres(), dto.getApellidos(), dto.getDui(), dto.getTelefono());
        ConfiguracionOperador config = ConfiguracionOperador.crear(
                dto.getPerfil(), dto.getIp(), dto.getCorrelativo(), dto.getAtenderCasosEspeciales());
        Usuario usuario = Usuario.inicializar(
                dto.getIdSucursal(), dto.getIdPuesto(), null,
                dto.getContrasena(), dto.getEstado() != null ? Estado.fromCodigo(dto.getEstado()) : null,
                datos, config);
        if (dto.getPerfilCreador() != null && !dto.getPerfilCreador().isBlank())
            usuario.asignarPerfilCreador(dto.getPerfilCreador());
        return usuario;
    }

    @Mapping(source = "identificador", target = "id")
    @Mapping(source = "nombreSucursal", target = "nombreSucursal")
    @Mapping(source = "nombrePuesto", target = "nombrePuesto")
    @Mapping(source = "auditoria.usuarioCreacion", target = "usuarioCreacion")
    @Mapping(source = "auditoria.fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "auditoria.usuarioModificacion", target = "usuarioModificacion")
    @Mapping(source = "auditoria.fechaModificacion", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "foto", target = "foto")
    UsuarioResponseDTO toResponse(Usuario usuario);

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
