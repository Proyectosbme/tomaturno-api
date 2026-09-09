package com.empresa.tomaturno.framework.adapters.input.controller;

import com.empresa.tomaturno.estadooperador.application.command.port.input.EstadoOperadorCommandInputPort;
import com.empresa.tomaturno.estadooperador.application.query.port.input.EstadoOperadorQueryInputPort;
import com.empresa.tomaturno.estadooperador.dominio.entity.EstadoOperador;
import com.empresa.tomaturno.framework.adapters.input.dto.DescansoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.EstadoOperadorInputMapper;
import com.empresa.tomaturno.framework.adapters.config.TurnoAutomaticoOrquestador;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Estado operativo del operador (abrir/cerrar/descanso) — aplica a cualquier tipo de
 * operador (caja, atención, asesor...), no solo a quien maneja una caja física.
 *
 * Además de cambiar el estado del operador, este controller orquesta el llamado/cierre
 * automático del turno del operador (cuando se activa, quita descanso, entra a
 * descanso o se cierra), reutilizando {@link TurnoAutomaticoOrquestador}.
 */
@Path("/estado-operador")
@Authenticated
public class EstadoOperadorController {

    private final EstadoOperadorCommandInputPort commandPort;
    private final EstadoOperadorQueryInputPort queryPort;
    private final EstadoOperadorInputMapper mapper;
    private final TurnoAutomaticoOrquestador turnoAutomaticoOrquestador;

    public EstadoOperadorController(EstadoOperadorCommandInputPort commandPort,
                                EstadoOperadorQueryInputPort queryPort,
                                EstadoOperadorInputMapper mapper,
                                TurnoAutomaticoOrquestador turnoAutomaticoOrquestador) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.mapper = mapper;
        this.turnoAutomaticoOrquestador = turnoAutomaticoOrquestador;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "OPERADOR", "ADMIN", "SUBADMIN" })
    public Response buscarVigente(
            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("idSucursal") Long idSucursal) {
        EstadoOperador vigente = queryPort.buscarVigente(idUsuario, idSucursal);
        if (vigente == null) {
            return Response.noContent().build();
        }
        return Response.ok(mapper.toResponse(vigente)).build();
    }

    @PUT
    @Path("/abrir")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("OPERADOR")
    public Response abrir(
            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("idPuesto") Long idPuesto) {
        EstadoOperador estadoOperador = commandPort.abrirOperador(idUsuario, idSucursal, idPuesto);
        turnoAutomaticoOrquestador.intentarLlamadoAutomatico(idSucursal, idPuesto, idSucursal, idUsuario);
        return Response.ok(mapper.toResponse(estadoOperador)).build();
    }

    @PUT
    @Path("/cerrar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("OPERADOR")
    public Response cerrar(
            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("idPuesto") Long idPuesto) {
        turnoAutomaticoOrquestador.finalizarTurnoActivoSiExiste(idSucursal, idPuesto, idSucursal);
        EstadoOperador estadoOperador = commandPort.cerrarOperador(idUsuario, idSucursal, idPuesto);
        return Response.ok(mapper.toResponse(estadoOperador)).build();
    }

    @PUT
    @Path("/descanso")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("OPERADOR")
    public Response iniciarDescanso(
            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("idPuesto") Long idPuesto,
            @Valid DescansoRequestDTO dto) {
        turnoAutomaticoOrquestador.finalizarTurnoActivoSiExiste(idSucursal, idPuesto, idSucursal);
        EstadoOperador estadoOperador = commandPort.iniciarDescanso(idUsuario, idSucursal, idPuesto, dto.getIdTipoDescanso(), dto.getComentario());
        return Response.ok(mapper.toResponse(estadoOperador)).build();
    }

    @PUT
    @Path("/descanso/quitar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("OPERADOR")
    public Response quitarDescanso(
            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("idPuesto") Long idPuesto) {
        EstadoOperador estadoOperador = commandPort.quitarDescanso(idUsuario, idSucursal, idPuesto);
        turnoAutomaticoOrquestador.intentarLlamadoAutomatico(idSucursal, idPuesto, idSucursal, idUsuario);
        return Response.ok(mapper.toResponse(estadoOperador)).build();
    }
}
