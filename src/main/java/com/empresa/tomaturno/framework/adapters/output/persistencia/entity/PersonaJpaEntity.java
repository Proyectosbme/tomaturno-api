package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "persona", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dui", nullable = false, unique = true)
    private String dui;

    private String nombres;
    private String apellidos;

    @Column(name = "fechanacimiento")
    private LocalDate fechaNacimiento;

    private String sexo;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;
}
