package com.empresa.tomaturno.sucursal.dominio.vo;

import com.empresa.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;

public enum Estado {

	ACTIVO(1, "ACTIVO"),
	INACTIVO(0, "INACTIVO");


	private final Integer codigo;
	private final String descripcion;


	Estado(Integer codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public Integer getCodigo() {
		return codigo;
	}


	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Busca un estado por su código numérico.
	 *
	 * @param codigo Código numérico a buscar
	 * @return Estado correspondiente al código
	 * @throws IllegalArgumentException si el código no es válido
	 */
	public static Estado fromCodigo(Integer codigo) {
		if (codigo == null) {
			throw new SucursalValidationException("Estado no puede ser nulo o vacío");
		}

		for (Estado estado : values()) {
			if (estado.codigo.compareTo(codigo)==0) {
				return estado;
			}
		}

		throw new SucursalValidationException(
				"Estado inválido: '" + codigo + "'. Valores válidos: 1, 0"
		);
	}

	/**
	 * Busca un estado por su descripción.
	 *
	 * @param descripcion Descripción a buscar (insensible a mayúsculas)
	 * @return Estado correspondiente a la descripción
	 * @throws SucursalValidationException si la descripción no es válida
	 */
	public static Estado fromDescripcion(String descripcion) {
		if (descripcion == null || descripcion.isBlank()) {
			throw new SucursalValidationException("La descripción del estado no puede ser nula o vacía");
		}
		String descNorm = quitarTildes(descripcion.trim()).toUpperCase();
		for (Estado estado : values()) {
			if (estado.name().equals(descNorm)) {
				return estado;
			}
		}
		throw new SucursalValidationException(
			"Estado inválido: '" + descripcion + "'. Valores válidos: ACTIVO, INACTIVO"
		);
	}

	private static String quitarTildes(String input) {
		String norm = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
		return norm.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
