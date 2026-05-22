package com.empresa.tomaturno.shared.clases;


public enum CatalogoEnum {

	/**
	 * Estado activo: el elemento está disponible para operaciones de negocio.
	 */
	ESTADOS_TURNOS(1, "ESTADOS_TURNOS");



	/**
	 * Código numérico del estado (1 para ACTIVO, 0 para INACTIVO).
	 * Obligatorio, usado para validaciones y persistencia.
	 */
	private final Integer codigo;
	/**
	 * Descripción textual del estado.
	 * Obligatorio, usado para mostrar en interfaces y validaciones.
	 */
	private final String descripcion;


	/**
	 * Constructor privado para inicializar el enum Estado.
	 * @param codigo Código numérico del estado (obligatorio)
	 * @param descripcion Descripción textual del estado (obligatorio)
	 */
	CatalogoEnum(Integer codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	/**
	 * Obtiene el código numérico del estado.
	 * @return código numérico (1 para ACTIVO, 0 para INACTIVO)
	 */
	public Integer getCodigo() {
		return codigo;
	}


	/**
	 * Obtiene la descripción textual del estado.
	 * @return descripción del estado
	 */
	public String getDescripcion() {
		return descripcion;
	}

	public static CatalogoEnum fromCodigo(Integer codigo) {
    if (codigo == null) {
        throw new IllegalArgumentException("Estado no puede ser nulo o vacío");
    }

    for (CatalogoEnum estado : CatalogoEnum.values()) {
        if (estado.codigo.compareTo(codigo) == 0) {
            return estado;
        }
    }

    throw new IllegalArgumentException(
        "Estado inválido: '" + codigo + "'. Valores válidos: 1, 0"
    );
}

public static CatalogoEnum fromDescripcion(String descripcion) {
    if (descripcion == null || descripcion.isBlank()) {
        throw new IllegalArgumentException("La descripción del estado no puede ser nula o vacía");
    }
    String descNorm = quitarTildes(descripcion.trim()).toUpperCase();
    for (CatalogoEnum estado : CatalogoEnum.values()) {
        if (estado.name().equals(descNorm)) {
            return estado;
        }
    }
    throw new IllegalArgumentException(
        "Estado inválido: '" + descripcion + "'. Valores válidos: ACTIVO, INACTIVO"
    );
}


	/**
	 * Quita tildes de una cadena para normalizar la comparación de descripciones.
	 * <p>
	 * Regla técnica: Usado internamente para asegurar que la búsqueda por descripción sea insensible a tildes.
	 * </p>
	 * @param input Cadena a normalizar
	 * @return Cadena sin tildes
	 */
	private static String quitarTildes(String input) {
		String norm = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
		return norm.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
