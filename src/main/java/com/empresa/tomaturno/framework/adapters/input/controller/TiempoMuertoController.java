package com.empresa.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.input.dto.TiempoMuertoResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.TiempoMuertoInputMapper;
import com.empresa.tomaturno.framework.adapters.output.persistencia.repository.VwUsuarioEstadoOperadorJpaRepository;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Historial de estado del operador de HOY (tomaturno.vwusuarioestadooperador), para la pantalla
 * "Tiempos muertos" de Monitoreo: cuánto tiempo pasó cada operador ACTIVA vs en DESCANSO
 * (y de qué tipo). Reporte de solo lectura — va directo de la vista al DTO, sin dominio
 * ni puertos, siguiendo el mismo criterio que /turnos/hoy.
 */
@Path("/tiempos-muertos")
@Authenticated
public class TiempoMuertoController {

    private final VwUsuarioEstadoOperadorJpaRepository repository;
    private final TiempoMuertoInputMapper mapper;

    public TiempoMuertoController(VwUsuarioEstadoOperadorJpaRepository repository, TiempoMuertoInputMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "OPERADOR", "ADMIN", "SUBADMIN" })
    public List<TiempoMuertoResponseDTO> buscar(
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("idUsuario") Long idUsuario) {
        var entidades = idUsuario != null
                ? repository.buscarPorUsuario(idUsuario, idSucursal)
                : repository.buscarPorSucursal(idSucursal);
        return entidades.stream().map(mapper::toResponse).toList();
    }
}
