package com.coop.tomaturno.framework.adapters.output.persistencia.entity;


import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class DetalleColaPK implements Serializable {

    private Long idCola;
    private Integer idSucursal;
    private Long idDetalle;

}
