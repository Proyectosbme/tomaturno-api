package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "turno", schema = "proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoJpaEntity {

    @EmbeddedId
    private TurnoJpaEntityPK idpk;

    /** Referencia Long auto-generada para idTurnoRelacionado */
    private Long id;

    @Column(name = "idsucursal", insertable = false, updatable = false)
    private Long idSucursal;

    @Column(name = "fechacrecion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "codigoturno", insertable = false, updatable = false)
    private String codigoTurno;

    @Column(name = "fechallamada")
    private LocalDateTime fechaLlamada;

    @Column(name = "fechafinalizacion")
    private LocalDateTime fechaFinalizacion;

    @Column(name = "idcola")
    private Long idCola;

    @Column(name = "iddetalle")
    private Long idDetalle;

    private Integer estado;

    @Column(name = "idturnorelacionado")
    private Long idTurnoRelacionado;

    @Column(name = "idpuesto")
    private Long idPuesto;

    @Column(name = "idsucursalpuesto")
    private Long idSucursalPuesto;

    @Column(name = "idusuario")
    private Long idUsuario;

    @Column(name = "idpersona")
    private Long idPersona;

    @Column(name = "tipocasoespecial")
    private Integer tipoCasoEspecial;
}
