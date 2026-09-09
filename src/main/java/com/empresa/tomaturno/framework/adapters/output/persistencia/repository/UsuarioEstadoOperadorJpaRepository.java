package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioEstadoOperadorJpaEntity;

@ApplicationScoped
public class UsuarioEstadoOperadorJpaRepository
        implements PanacheRepositoryBase<UsuarioEstadoOperadorJpaEntity, Long> {

    public UsuarioEstadoOperadorJpaEntity buscarVigente(Long idUsuario, Long idSucursal) {
        return find("idUsuario = ?1 and idSucursal = ?2 and fechaFin is null", idUsuario, idSucursal)
                .firstResult();
    }

    public List<UsuarioEstadoOperadorJpaEntity> buscarHistorial(Long idUsuario, Long idSucursal,
            LocalDateTime desde, LocalDateTime hasta) {
        return list(
                "idUsuario = ?1 and idSucursal = ?2 and fechaInicio >= ?3 and fechaInicio < ?4 order by fechaInicio desc",
                idUsuario, idSucursal, desde, hasta);
    }
}
