package com.coop.tomaturno.cola.application.command.usecase;

import java.util.ArrayList;
import java.util.List;

import com.coop.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.coop.tomaturno.cola.application.query.port.output.ColaQueryRepository;
import com.coop.tomaturno.cola.dominio.entity.Cola;
import com.coop.tomaturno.cola.dominio.entity.Detalle;

public class ReplicarColasUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaQueryRepository colaQueryRepository;

    public ReplicarColasUseCase(ColaCommandRepository colaCommandRepository,
            ColaQueryRepository colaQueryRepository) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaQueryRepository = colaQueryRepository;
    }

    public ResultadoReplicacion ejecutar(Long idSucursalOrigen, Long idSucursalDestino) {
        // Traer todas las colas con detalles de la sucursal origen
        List<Cola> colasOrigen = colaQueryRepository.buscarConDetallesPorSucursal(idSucursalOrigen);

        List<String> copiadas = new ArrayList<>();
        List<String> saltadas = new ArrayList<>();

        for (Cola cola : colasOrigen) {
            // Si ya existe una cola con el mismo nombre en destino → saltar
            boolean existeNombre = colaQueryRepository.existeNombreEnSucursal(
                    idSucursalDestino, cola.getNombre());

            if (existeNombre) {
                replicarDetallesFaltantes(cola, idSucursalDestino);
                saltadas.add(cola.getNombre());
                continue;
            }

            colaCommandRepository.replicarCola(cola, idSucursalDestino);
            copiadas.add(cola.getNombre());
        }

        return new ResultadoReplicacion(copiadas, saltadas);
    }

    private void replicarDetallesFaltantes(Cola colaOrigen, Long idSucursalDestino) {
        List<Cola> colasDestino = colaQueryRepository.buscarPorFiltro(null, idSucursalDestino, colaOrigen.getNombre());
        if (colasDestino.isEmpty() || colaOrigen.getDetalles() == null) return;
        Cola colaDestino = colasDestino.get(0);
        for (Detalle detalle : colaOrigen.getDetalles()) {
            boolean existeDetalle = colaQueryRepository.existeNombreDetalleEnCola(
                    colaDestino.getIdentificador(), idSucursalDestino, detalle.getNombre());
            if (!existeDetalle) {
                colaCommandRepository.guardarDetalle(colaDestino.getIdentificador(), idSucursalDestino, detalle);
            }
        }
    }

    /** DTO de resultado embebido en el use case */
    public static class ResultadoReplicacion {
        private final List<String> colasCopidas;
        private final List<String> colasSaltadas;

        public ResultadoReplicacion(List<String> colasCopidas, List<String> colasSaltadas) {
            this.colasCopidas = colasCopidas;
            this.colasSaltadas = colasSaltadas;
        }

        public int getTotalCopiadas() { return colasCopidas.size(); }
        public int getTotalSaltadas() { return colasSaltadas.size(); }
        public List<String> getColasCopidas() { return colasCopidas; }
        public List<String> getColasSaltadas() { return colasSaltadas; }
    }
}
