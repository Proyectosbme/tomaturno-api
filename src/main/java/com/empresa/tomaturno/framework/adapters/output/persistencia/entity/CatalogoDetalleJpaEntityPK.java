package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import java.io.Serializable;


import jakarta.persistence.Embeddable;
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
public class CatalogoDetalleJpaEntityPK implements Serializable {

    private Long idCatalogo;
    private Long idDetalle;

}