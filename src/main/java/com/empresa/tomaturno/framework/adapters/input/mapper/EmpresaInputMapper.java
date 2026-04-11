package com.empresa.tomaturno.framework.adapters.input.mapper;

import org.mapstruct.Mapper;

import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.framework.adapters.input.dto.EmpresaResponseDTO;

@Mapper(componentModel = "cdi")
public interface EmpresaInputMapper {

    EmpresaResponseDTO toResponse(Empresa empresa);
}
