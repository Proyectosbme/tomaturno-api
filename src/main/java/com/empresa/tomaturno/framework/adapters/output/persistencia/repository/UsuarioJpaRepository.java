package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.UsuarioJpaEntityPK;

@ApplicationScoped
public class UsuarioJpaRepository implements PanacheRepositoryBase<UsuarioJpaEntity, UsuarioJpaEntityPK> {

    public Long obtenerSiguienteId(Long idSucursal) {
        Long maxId = find("idpk.idSucursal = ?1", idSucursal)
                .stream()
                .mapToLong(u -> u.getIdpk().getId())
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    public UsuarioJpaEntity buscarPorIdUsuarioYSucursal(Long idUsuario, Long idSucursal) {
        return find("idpk.id = ?1 and idpk.idSucursal = ?2", idUsuario, idSucursal).firstResult();
    }

    public boolean existeCodigoEnSucursal(Long idSucursal, String codigoUsuario) {
        return count("idpk.idSucursal = ?1 and upper(codigoUsuario) = upper(?2)",
                idSucursal, codigoUsuario) > 0;
    }

    public UsuarioJpaEntity buscarPorCodigo(String codigoUsuario) {
        return find("upper(codigoUsuario) = upper(?1)", codigoUsuario).firstResult();
    }

    public UsuarioJpaEntity buscarPorCodigoYSucursal(String codigoUsuario, Long idSucursal) {
        return find("upper(codigoUsuario) = upper(?1) and idpk.idSucursal = ?2", codigoUsuario, idSucursal).firstResult();
    }

    public List<UsuarioJpaEntity> buscarPorFiltros(Long idSucursal, String codigoUsuario, String nombre) {
        if (idSucursal == null && (codigoUsuario == null || codigoUsuario.isBlank())
                && (nombre == null || nombre.isBlank())) {
            return List.of();
        }

        StringBuilder query = new StringBuilder("1=1");
        int paramIndex = 1;
        List<Object> params = new ArrayList<>();

        if (idSucursal != null) {
            query.append(" and idpk.idSucursal = ?").append(paramIndex++);
            params.add(idSucursal);
        }
        if (codigoUsuario != null && !codigoUsuario.isBlank()) {
            query.append(" and upper(codigoUsuario) like ?").append(paramIndex++);
            params.add("%" + codigoUsuario.toUpperCase() + "%");
        }
        if (nombre != null && !nombre.isBlank()) {
            query.append(" and (upper(nombres) like ?").append(paramIndex)
                    .append(" or upper(apellidos) like ?").append(paramIndex).append(")");
            params.add("%" + nombre.toUpperCase() + "%");
            paramIndex++;
        }

        return list(query.toString(), params.toArray());
    }
}
