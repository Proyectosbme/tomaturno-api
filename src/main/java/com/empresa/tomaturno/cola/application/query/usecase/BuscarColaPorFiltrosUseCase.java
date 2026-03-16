package com.empresa.tomaturno.cola.application.query.usecase;

import java.util.List;

import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;


public class BuscarColaPorFiltrosUseCase {

    private final ColaQueryRepository colaRepository;

    public BuscarColaPorFiltrosUseCase(ColaQueryRepository colaRepository) {
        this.colaRepository = colaRepository;
    }

    /**
     * Busca colas por los filtros dados.
     *
     * @param id     el ID de la cola a filtrar
     * @param idSucursal el ID de la sucursal a filtrar
     * @param nombre el nombre de la cola a filtrar
     * @return una lista de colas que coinciden con los filtros
     */
    public List<Cola> ejecutar(Long id, Long idSucursal, String nombre) {
        return colaRepository.buscarPorFiltro(id, idSucursal, nombre);
    }

}
