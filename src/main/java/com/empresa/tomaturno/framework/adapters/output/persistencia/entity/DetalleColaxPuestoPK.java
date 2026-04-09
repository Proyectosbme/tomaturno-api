package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class DetalleColaxPuestoPK implements Serializable {

    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idCola;
    private Long idDetalle;
    private Long idSucursalCola;
}
