package com.empresa.tomaturno.cola.dominio.especificacion;

import java.util.List;

import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.cola.dominio.validador.ValidadorNulosVacios;

/**
 * Regla de negocio "no existe otra cola con este nombre en la sucursal", evaluada contra
 * las colas ya cargadas de la sucursal — no consulta el repositorio por su cuenta. El
 * caller (caso de uso) trae la lista con ColaQueryRepository.buscarConDetallesPorSucursal.
 */
public final class NombreColaUnicoEspec extends AbstractEspecificacion<String> {

    private final List<Cola> colasExistentes;

    public NombreColaUnicoEspec(List<Cola> colasExistentes) {
        this.colasExistentes = ValidadorNulosVacios
                .variable(colasExistentes, "colasExistentes", ColaValidationException::new)
                .noNulo()
                .obtener();
    }

    @Override
    public boolean esSatisfechaPor(String nombre) {
        return colasExistentes.stream().noneMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public void verificar(String nombre) {
        if (!esSatisfechaPor(nombre)) {
            throw new ColaValidationException("Ya existe una cola con el nombre '" + nombre + "' en esta sucursal");
        }
    }
}
