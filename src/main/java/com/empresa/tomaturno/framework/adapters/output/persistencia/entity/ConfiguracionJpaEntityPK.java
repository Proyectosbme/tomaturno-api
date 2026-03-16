package com.coop.tomaturno.framework.adapters.output.persistencia.entity;

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
public class ConfiguracionJpaEntityPK implements Serializable {

    private Long idConfiguracion;
    private Long idSucursal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguracionJpaEntityPK that = (ConfiguracionJpaEntityPK) o;
        return Objects.equals(idConfiguracion, that.idConfiguracion)
                && Objects.equals(idSucursal, that.idSucursal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idConfiguracion, idSucursal);
    }
}
