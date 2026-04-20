package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sucursal", schema = "proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SucursalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "usuario_creacion", nullable = false, length = 50)
    private String usuarioCreacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_modificacion", length = 50)
    private String usuarioModificacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "estado", nullable = false)
    private Integer estado;
}
