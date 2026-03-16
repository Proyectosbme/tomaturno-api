package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaxPuestoJpaEntity;
import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.DetalleColaxPuestoPK;

@ApplicationScoped
public class DetalleColaxPuestoJpaRepository
        implements PanacheRepositoryBase<DetalleColaxPuestoJpaEntity, DetalleColaxPuestoPK> {

    public List<DetalleColaxPuestoJpaEntity> buscarPorPuesto(Long idPuesto, Long idSucursalPuesto) {
        return list("id.idPuesto = ?1 and id.idSucursalPuesto = ?2", idPuesto, idSucursalPuesto);
    }

    public boolean existeAsignacion(Long idPuesto, Long idSucursalPuesto,
                                     Long idCola, Long idDetalle, Long idSucursalCola) {
        return count("id.idPuesto = ?1 and id.idSucursalPuesto = ?2 and id.idCola = ?3 " +
                "and id.idDetalle = ?4 and id.idSucursalCola = ?5",
                idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola) > 0;
    }

    public void eliminarAsignacion(Long idPuesto, Long idSucursalPuesto,
                                    Long idCola, Long idDetalle, Long idSucursalCola) {
        delete("id.idPuesto = ?1 and id.idSucursalPuesto = ?2 and id.idCola = ?3 " +
                "and id.idDetalle = ?4 and id.idSucursalCola = ?5",
                idPuesto, idSucursalPuesto, idCola, idDetalle, idSucursalCola);
    }
}
