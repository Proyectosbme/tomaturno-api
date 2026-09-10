package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioEstadoOperadorJpaEntity;

@ApplicationScoped
public class UsuarioEstadoOperadorJpaRepository
        implements PanacheRepositoryBase<UsuarioEstadoOperadorJpaEntity, Long> {

    public UsuarioEstadoOperadorJpaEntity buscarVigente(Long idUsuario, Long idSucursal) {
        return find("idUsuario = ?1 and idSucursal = ?2 and fechaFin is null", idUsuario, idSucursal)
                .firstResult();
    }
}
