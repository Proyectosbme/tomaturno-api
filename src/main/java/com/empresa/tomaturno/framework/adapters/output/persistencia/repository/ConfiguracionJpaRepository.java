package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.ConfiguracionJpaEntityPK;

@ApplicationScoped
public class ConfiguracionJpaRepository
        implements PanacheRepositoryBase<ConfiguracionJpaEntity, ConfiguracionJpaEntityPK> {

    public Long obtenerSiguienteId(Long idSucursal) {
        Long maxId = find("idpk.idSucursal = ?1", idSucursal)
                .stream()
                .mapToLong(c -> c.getIdpk().getIdConfiguracion())
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    public List<ConfiguracionJpaEntity> buscarPorSucursal(Long idSucursal) {
        return list("idpk.idSucursal = ?1 order by nombre asc", idSucursal);
    }

    public ConfiguracionJpaEntity buscarPorIdYSucursal(Long idConfiguracion, Long idSucursal) {
        return find("idpk.idConfiguracion = ?1 and idpk.idSucursal = ?2",
                idConfiguracion, idSucursal).firstResult();
    }

    public ConfiguracionJpaEntity buscarPorNombreYSucursal(Long idSucursal, String nombre) {
        return find("idpk.idSucursal = ?1 and upper(nombre) = upper(?2)", idSucursal, nombre).firstResult();
    }
}
