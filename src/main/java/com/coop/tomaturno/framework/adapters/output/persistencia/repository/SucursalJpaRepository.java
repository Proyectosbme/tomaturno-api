package com.coop.tomaturno.framework.adapters.output.persistencia.repository;

import com.coop.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SucursalJpaRepository implements PanacheRepository<SucursalJpaEntity> {

	public List<SucursalJpaEntity> buscarPorNombre(String nombre) {
		if (nombre == null || nombre.isBlank()) {
			return listAll();
		}
		return list("upper(nombre) like ?1", "%" + nombre.toUpperCase() + "%");
	}
}
