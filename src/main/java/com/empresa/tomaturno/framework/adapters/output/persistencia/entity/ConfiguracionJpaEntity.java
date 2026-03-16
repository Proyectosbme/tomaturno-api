package com.coop.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionJpaEntity {

    @EmbeddedId
    private ConfiguracionJpaEntityPK idpk;

    @Column(insertable = false, updatable = false)
    private Long idConfiguracion;

    @Column(insertable = false, updatable = false)
    private Long idSucursal;

    private String nombre;
    private Integer parametro;
    private String valorTexto;
    private String descripcion;
    private Integer estado;
    private LocalDateTime fechaCreacion;
    private String userCreacion;
    private String userModificacion;
    private LocalDateTime fechaModificacion;
}
