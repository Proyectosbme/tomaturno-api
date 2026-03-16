package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cola", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColaJpaEntity {

    @EmbeddedId
    private ColaJpaEntityPK idpk;

    @Column(insertable = false, updatable = false)
    private Long id;

    @Column(insertable = false, updatable = false)
    private Long idSucursal;

    private String nombre;
    private String codigo;
    private Long prioridad;
    private LocalDateTime fechaCreacion;
    private String userCreacion;
    private String userModificacion;
    private LocalDateTime fechaModificacion;
    private Integer estado;

}
