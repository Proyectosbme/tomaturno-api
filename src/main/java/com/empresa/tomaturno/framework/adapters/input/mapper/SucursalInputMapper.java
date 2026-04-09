package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empresa.tomaturno.framework.adapters.input.dto.SucursalRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.SucursalResponseDTO;
import com.empresa.tomaturno.shared.clases.Estado;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;
import com.empresa.tomaturno.sucursal.dominio.vo.Contacto;


@Mapper(componentModel = "cdi")
public interface SucursalInputMapper {

    default Sucursal toSucursal(SucursalRequestDTO dto) {
        Contacto contacto = Contacto.crear(dto.getTelefono(), dto.getCorreo(), dto.getDireccion());
        return Sucursal.crear(dto.getNombre(), contacto, Estado.fromCodigo(dto.getEstado()));
    }

    @Mapping(source = "identificador", target = "codigo")
    @Mapping(source = "contacto.telefono", target = "telefono")
    @Mapping(source = "contacto.correo", target = "correo")
    @Mapping(source = "contacto.direccion", target = "direccion")
    @Mapping(source = "auditoria.usuarioCreacion", target = "usuarioCreacion")
    @Mapping(source = "auditoria.fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "auditoria.usuarioModificacion", target = "usuarioModificacion")
    @Mapping(source = "auditoria.fechaModificacion", target = "fechaModificacion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    SucursalResponseDTO toSucursalResponseDTO(Sucursal sucursal);

    @Named("estadoToCodigo")
    default Integer estadoToCodigo(Estado estado) {
        return estado != null ? estado.getCodigo() : null;
    }
}
