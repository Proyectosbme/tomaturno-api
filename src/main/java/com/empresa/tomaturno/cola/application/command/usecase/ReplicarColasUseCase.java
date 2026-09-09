package com.empresa.tomaturno.cola.application.command.usecase;

import java.util.ArrayList;
import java.util.List;

import com.empresa.tomaturno.cola.application.command.dto.ResultadoReplicacion;
import com.empresa.tomaturno.cola.application.command.port.output.ColaCommandRepository;
import com.empresa.tomaturno.cola.application.command.port.output.ColaGatewayPort;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.especificacion.NombreColaUnicoEspec;

public class ReplicarColasUseCase {

    private final ColaCommandRepository colaCommandRepository;
    private final ColaGatewayPort colaGatewayPort;

    public ReplicarColasUseCase(ColaCommandRepository colaCommandRepository,
            ColaGatewayPort colaGatewayPort) {
        this.colaCommandRepository = colaCommandRepository;
        this.colaGatewayPort = colaGatewayPort;
    }

    public ResultadoReplicacion ejecutar(Long idSucursalOrigen, Long idSucursalDestino , String usuario) {
        List<String> detallesCopiados = new ArrayList<>();
        List<Cola> colasOrigen = colaGatewayPort.buscarConDetallesPorSucursal(idSucursalOrigen);
        List<Cola> colasDestino = colaGatewayPort.buscarConDetallesPorSucursal(idSucursalDestino);
        NombreColaUnicoEspec nombreColaUnicoEnDestino = new NombreColaUnicoEspec(colasDestino);

        List<String> copiadas = new ArrayList<>();
        List<String> saltadas = new ArrayList<>();

        for (Cola cola : colasOrigen) {
            // Si ya existe una cola con el mismo nombre en destino → saltar
            if (!nombreColaUnicoEnDestino.esSatisfechaPor(cola.getNombre())) {
                String detalleCopiados = replicarDetallesFaltantes(cola, colasDestino, idSucursalDestino);
                if (detalleCopiados != null) {
                    detallesCopiados.add(detalleCopiados);
                }
                saltadas.add(cola.getNombre());
                continue;
            }

            colaCommandRepository.replicarCola(cola, idSucursalDestino, usuario);
            copiadas.add(cola.getNombre());
        }

        return new ResultadoReplicacion(copiadas, saltadas, detallesCopiados);
    }

    private String replicarDetallesFaltantes(Cola colaOrigen, List<Cola> colasDestino, Long idSucursalDestino) {
        StringBuilder response = new StringBuilder(colaOrigen.getNombre());
        Integer contador = 0;
        Cola colaDestino = colasDestino.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(colaOrigen.getNombre()))
                .findFirst()
                .orElse(null);
        if (colaDestino == null || colaOrigen.getDetalles() == null) return null;
        for (Detalle detalle : colaOrigen.getDetalles()) {
            if (!colaDestino.existeDetalleConNombre(detalle.getNombre())) {
                response.append(" - Detalle: ").append(detalle.getNombre());
                contador++;
                colaCommandRepository.guardarDetalle(colaDestino.getIdentificador(), idSucursalDestino, detalle);
            }
        }
        return contador > 0 ? response.toString() : null;
    }
}
