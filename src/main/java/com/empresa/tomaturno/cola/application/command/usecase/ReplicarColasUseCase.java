package com.empresa.tomaturno.cola.application.command.usecase;

import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.cola.DTO.ResultadoReplicacion;
import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;

public class ReplicarColasUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaQueryRepository colaQueryRepository;

    public ReplicarColasUseCase(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public ResultadoReplicacion ejecutar(Long idSucursalOrigen, Long idSucursalDestino , String usuario) {
        List<String> detallesCopiados = new ArrayList<>();
        // Traer todas las colas con detalles de la sucursal origen
        List<Cola> colasOrigen = colaQueryRepository.buscarConDetallesPorSucursal(idSucursalOrigen);

        List<String> copiadas = new ArrayList<>();
        List<String> saltadas = new ArrayList<>();

        for (Cola cola : colasOrigen) {
            // Si ya existe una cola con el mismo nombre en destino → saltar
            boolean existeNombre = colaQueryRepository.existeNombreEnSucursal(
                    idSucursalDestino, cola.getNombre());

            if (existeNombre) {
               String detalleCopiados = replicarDetallesFaltantes(cola, idSucursalDestino);
               if(detalleCopiados != null) {
                   detallesCopiados.add(detalleCopiados);
               }
                saltadas.add(cola.getNombre());
                continue;
            }

            colaCommandRepository.replicarCola(cola, idSucursalDestino,usuario);
            copiadas.add(cola.getNombre());
        }

        return new ResultadoReplicacion(copiadas, saltadas, detallesCopiados);
    }

    private String replicarDetallesFaltantes(Cola colaOrigen, Long idSucursalDestino) {
        StringBuilder response = new StringBuilder(colaOrigen.getNombre());
        Integer contador = 0;
        List<Cola> colasDestino = colaQueryRepository.buscarPorFiltro(null, idSucursalDestino, colaOrigen.getNombre());
        if (colasDestino.isEmpty() || colaOrigen.getDetalles() == null) return null;
        Cola colaDestino = colasDestino.get(0);
        for (Detalle detalle : colaOrigen.getDetalles()) {
            boolean existeDetalle = colaQueryRepository.existeNombreDetalleEnCola(
                    colaDestino.getIdentificador(), idSucursalDestino, detalle.getNombre());
            if (!existeDetalle) {
                response.append(" - Detalle: ").append(detalle.getNombre());
                contador++;
                colaCommandRepository.guardarDetalle(colaDestino.getIdentificador(), idSucursalDestino, detalle);
            }
        }
        return contador>0 ? response.toString() : null;
    }
}
