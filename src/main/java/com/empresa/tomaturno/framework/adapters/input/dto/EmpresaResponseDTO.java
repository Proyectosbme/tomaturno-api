package com.empresa.tomaturno.framework.adapters.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponseDTO {

    private Long id;
    private String nombre;
    private byte[] banner;
    private byte[] logo;
}
