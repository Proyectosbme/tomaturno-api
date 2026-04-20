package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario", schema = "proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioJpaEntity {

    @EmbeddedId
    private UsuarioJpaEntityPK idpk;

    @Column(insertable = false, updatable = false)
    private Long id;

    @Column(insertable = false, updatable = false)
    private Long idSucursal;

    private Integer correlativo;

    @Column(name = "atendercasosespeciales")
    private Integer atenderCasosEspeciales;

    private Long idPuesto;
    private String codigoUsuario;
    private String keycloakId;
    private String dui;
    private Integer estado;
    private String ip;

    @Column(name = "foto")
    private byte[] foto;
     private String telefono;
    private LocalDateTime fechaCreacion;
    private String userCreacion;
    private String userModificacion;
    private LocalDateTime fechaModificacion;
}
