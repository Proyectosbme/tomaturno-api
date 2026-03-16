package com.empresa.tomaturno.framework.adapters.output.persistencia.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.output.persistencia.entity.SucursalJpaEntity;

@ApplicationScoped
public class SucursalJpaRepository implements PanacheRepository<SucursalJpaEntity> {

	public List<SucursalJpaEntity> buscarPorNombre(String nombre) {
		if (nombre == null || nombre.isBlank()) {
			return listAll();
		}
		return list("upper(nombre) like ?1", "%" + nombre.toUpperCase() + "%");
	}
}
