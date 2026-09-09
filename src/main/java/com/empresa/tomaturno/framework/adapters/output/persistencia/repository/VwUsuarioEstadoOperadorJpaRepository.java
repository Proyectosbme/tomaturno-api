package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.VwUsuarioEstadoOperadorJpaEntity;

@ApplicationScoped
public class VwUsuarioEstadoOperadorJpaRepository implements PanacheRepositoryBase<VwUsuarioEstadoOperadorJpaEntity, Long> {

    public List<VwUsuarioEstadoOperadorJpaEntity> buscarPorSucursal(Long idSucursal) {
        return list("idSucursal = ?1 order by fechaInicio asc", idSucursal);
    }

    public List<VwUsuarioEstadoOperadorJpaEntity> buscarPorUsuario(Long idUsuario, Long idSucursal) {
        return list("idUsuario = ?1 and idSucursal = ?2 order by fechaInicio asc", idUsuario, idSucursal);
    }
}
