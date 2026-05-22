package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.tomaturno.framework.adapters.input.dto.TurnoResponseDTO;
import com.empresa.tomaturno.turno.dominio.entity.Turno;
import com.empresa.tomaturno.turno.dominio.vo.DetalleEstado;

import java.util.Arrays;

@Mapper(componentModel = "cdi", imports = { DetalleEstado.class, Arrays.class })
public interface TurnoInputMapper {

    @Mapping(target = "estado",
             expression = "java(turno.getEstado() != null ? (int) turno.getEstado().detalle() : null)")
    @Mapping(target = "descripcionEstado",
             expression = "java(turno.getEstado() != null ? Arrays.stream(DetalleEstado.values()).filter(e -> e.getValor() == turno.getEstado().detalle()).map(Enum::name).findFirst().orElse(null) : null)")
    TurnoResponseDTO toResponse(Turno turno);
}
