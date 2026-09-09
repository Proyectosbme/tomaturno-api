package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarioestadooperador", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEstadoOperadorJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUsuario;
    private Long idSucursal;
    private Long idPuesto;
    private Long idEstadoOperador;
    private Long idTipoDescanso;
    private String comentario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String userCreacion;
    private LocalDateTime fechaCreacion;
    private String userModificacion;
    private LocalDateTime fechaModificacion;
}
