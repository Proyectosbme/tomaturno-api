package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.VwTurnosHoyJpaEntity;

@ApplicationScoped
public class VwTurnosHoyJpaRepository implements PanacheRepositoryBase<VwTurnosHoyJpaEntity, Long> {

    public List<VwTurnosHoyJpaEntity> buscarTodos(Long idSucursal) {
        return list("idSucursalTicket = ?1 order by fechaCreacion desc", idSucursal);
    }

    public List<VwTurnosHoyJpaEntity> buscarPorUsuario(Long idUsuario, Long idSucursal) {
        return list("idUsuario = ?1 and idSucursalTicket = ?2 order by fechaCreacion desc", idUsuario, idSucursal);
    }
}
