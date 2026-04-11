package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empresa", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaJpaEntity {

    @Id
    private Long id;

    @Column(name = "nombre", length = 200)
    private String nombre;

    @Column(name = "banner")
    private byte[] banner;

    @Column(name = "logo")
    private byte[] logo;
}
