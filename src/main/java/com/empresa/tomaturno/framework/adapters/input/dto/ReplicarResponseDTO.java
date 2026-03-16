package com.empresa.tomaturno.framework.adapters.input.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplicarResponseDTO {
    private int totalCopiadas;
    private int totalSaltadas;
    private List<String> colasCopidas;
    private List<String> colasSaltadas;
}
