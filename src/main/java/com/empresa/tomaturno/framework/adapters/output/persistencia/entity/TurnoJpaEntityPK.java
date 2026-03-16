package com.coop.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TurnoJpaEntityPK implements Serializable {

    @Column(name = "idsucursal")
    private Long idSucursal;

    @Column(name = "fechacrecion")
    private LocalDateTime fechaCreacion;

    @Column(name = "codigoturno")
    private String codigoTurno;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnoJpaEntityPK that = (TurnoJpaEntityPK) o;
        return Objects.equals(idSucursal, that.idSucursal)
                && Objects.equals(fechaCreacion, that.fechaCreacion)
                && Objects.equals(codigoTurno, that.codigoTurno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSucursal, fechaCreacion, codigoTurno);
    }
}
