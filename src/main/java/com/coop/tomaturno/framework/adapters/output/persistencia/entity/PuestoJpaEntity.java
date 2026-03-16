package com.coop.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "puesto", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PuestoJpaEntity {

    @EmbeddedId
    private PuestoJpaEntityPK idpk;

    @Column(insertable = false, updatable = false)
    private Long id;

    @Column(insertable = false, updatable = false)
    private Long idSucursal;

    private String nombre;
    protected String nombreLlamada;
    private LocalDateTime fechaCreacion;
    private String userCreacion;
    private String userModificacion;
    private LocalDateTime fechaModificacion;
    private Integer estado;
}
