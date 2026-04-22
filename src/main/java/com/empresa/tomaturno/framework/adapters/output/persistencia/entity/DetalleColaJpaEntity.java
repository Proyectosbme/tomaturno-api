package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "coladetalle", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DetalleColaJpaEntity implements Serializable {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private DetalleColaPK id;

    private String nombre;

    private String codigo;

    private Integer estado;

    private LocalDateTime fechaCreacion;

    private String userCreacion;

    private String userModificacion;

    private LocalDateTime fechaModificacion;
}
