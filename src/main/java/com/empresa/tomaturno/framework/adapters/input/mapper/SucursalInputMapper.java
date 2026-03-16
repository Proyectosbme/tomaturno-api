package com.coop.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.coop.tomaturno.framework.adapters.input.dto.SucursalRequestDTO;
import com.coop.tomaturno.framework.adapters.input.dto.SucursalResponseDTO;
import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;
import com.coop.tomaturno.sucursal.dominio.vo.Estado;

@Mapper(componentModel = "cdi")
public interface SucursalInputMapper {

    @Mapping(ignore = true, target = "identificador")
    @Mapping(ignore = true, target = "auditoria")
    @Mapping(source = "telefono", target = "contacto.telefono")
    @Mapping(source = "correo", target = "contacto.correo")
    @Mapping(source = "direccion", target = "contacto.direccion")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "codigoToEstado")
    Sucursal toSucursal(SucursalRequestDTO sucursalRequestDTO);

    @Mapping(source = "identificador", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
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

    @Named("codigoToEstado")
    default Estado codigoToEstado(Integer codigo) {
        return codigo != null ? Estado.fromCodigo(codigo) : null;
    }
}
