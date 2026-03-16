package com.coop.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "detallecolaxpuesto", schema = "tomaturno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleColaxPuestoJpaEntity {

    @EmbeddedId
    private DetalleColaxPuestoPK id;

    private String userCreacion;
    private LocalDateTime fechaCreacion;
}
