package com.empresa.tomaturno.cola.dominio.especificacion;

import java.util.List;

import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.empresa.tomaturno.cola.dominio.validador.ValidadorNulosVacios;

/**
 * Regla de negocio "no existe otra cola con este código (una letra) en la sucursal", evaluada
 * contra las colas ya cargadas de la sucursal — no consulta el repositorio por su cuenta. El
 * caller (caso de uso) trae la lista con ColaQueryRepository.buscarConDetallesPorSucursal.
 */
public final class CodigoColaUnicoEspec extends AbstractEspecificacion<String> {

    private final List<Cola> colasExistentes;

    public CodigoColaUnicoEspec(List<Cola> colasExistentes) {
        this.colasExistentes = ValidadorNulosVacios
                .variable(colasExistentes, "colasExistentes", ColaValidationException::new)
                .noNulo()
                .obtener();
    }

    @Override
    public boolean esSatisfechaPor(String codigo) {
        return colasExistentes.stream().noneMatch(c -> c.getCodigo().equalsIgnoreCase(codigo));
    }

    @Override
    public void verificar(String codigo) {
        if (!esSatisfechaPor(codigo)) {
            throw new ColaValidationException("Ya existe una cola con el codigo '" + codigo + "' en esta sucursal");
        }
    }
}
