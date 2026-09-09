package com.empresa.tomaturno.framework.adapters.input.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.input.dto.SucursalRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.SucursalResponseDTO;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.vo.Auditoria;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;


@Mapper(componentModel = "cdi")
public interface SucursalInputMapper {

    /** Sucursal nueva: la auditoría de creación se arma aquí con el usuario autenticado. */
    default Sucursal toSucursal(SucursalRequestDTO dto, String usuario) {
        Contacto contacto = Contacto.crear(dto.getTelefono(), dto.getCorreo(), dto.getDireccion());
        return Sucursal.of(new Sucursal.Builder()
                .nombre(dto.getNombre())
                .contacto(contacto)
                .estado(Estado.fromCodigo(dto.getEstado()))
                .auditoriaCreacion(Auditoria.of(usuario, LocalDateTime.now())));
    }

    @Mapping(source = "identificador", target = "codigo")
    @Mapping(source = "contacto.telefono", target = "telefono")
    @Mapping(source = "contacto.correo", target = "correo")
    @Mapping(source = "contacto.direccion", target = "direccion")
    @Mapping(source = "auditoriaCreacion.usuario", target = "usuarioCreacion")
    @Mapping(source = "auditoriaCreacion.fecha", target = "fechaCreacion")
    @Mapping(source = "auditoriaModificacion.usuario", target = "usuarioModificacion")
    @Mapping(source = "auditoriaModificacion.fecha", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    SucursalResponseDTO toSucursalResponseDTO(Sucursal sucursal);

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
