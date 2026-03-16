package com.empresa.tomaturno.framework.adapters.output.persistencia.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DetalleColaxPuestoPK implements Serializable {

    private Long idPuesto;
    private Long idSucursalPuesto;
    private Long idCola;
    private Long idDetalle;
    private Long idSucursalCola;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleColaxPuestoPK that = (DetalleColaxPuestoPK) o;
        return Objects.equals(idPuesto, that.idPuesto)
                && Objects.equals(idSucursalPuesto, that.idSucursalPuesto)
                && Objects.equals(idCola, that.idCola)
                && Objects.equals(idDetalle, that.idDetalle)
                && Objects.equals(idSucursalCola, that.idSucursalCola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
    }
}
