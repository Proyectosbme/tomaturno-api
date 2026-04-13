package com.empresa.tomaturno.framework.adapters.input.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.empresa.tomaturno.framework.adapters.input.dto.CrearTurnoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.LlamarSiguienteTurnoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.LlamarTurnoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ReasignarTurnoRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.TurnoResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.TurnoInputMapper;
import com.empresa.tomaturno.turno.application.command.port.input.TurnoCommandInputPort;
import com.empresa.tomaturno.turno.application.query.port.input.TurnoQueryInputPort;
import com.empresa.tomaturno.turno.dominio.entity.Turno;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/turnos")
public class TurnoController {

    private final TurnoCommandInputPort turnoCommandInputPort;
    private final TurnoQueryInputPort turnoQueryInputPort;
    private final TurnoInputMapper turnoInputMapper;
    private final TurnoWebSocket turnoWebSocket;

    public TurnoController(TurnoCommandInputPort turnoCommandInputPort,
            TurnoQueryInputPort turnoQueryInputPort,
            TurnoInputMapper turnoInputMapper,
            TurnoWebSocket turnoWebSocket) {
        this.turnoCommandInputPort = turnoCommandInputPort;
        this.turnoQueryInputPort = turnoQueryInputPort;
        this.turnoInputMapper = turnoInputMapper;
        this.turnoWebSocket = turnoWebSocket;
    }

    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TurnoResponseDTO> buscar(
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("idCola") Long idCola,
            @QueryParam("idDetalle") Long idDetalle,
            @QueryParam("estado") Integer estado,
            @QueryParam("fecha") String fecha,
            @QueryParam("idPuesto") Long idPuesto,
            @QueryParam("idSucursalPuesto") Long idSucursalPuesto) {
        LocalDate localDate = fecha != null ? LocalDate.parse(fecha) : null;
        List<Turno> turnos = turnoQueryInputPort.buscarPorFiltro(idSucursal, idCola, idDetalle, estado, localDate,
                idPuesto, idSucursalPuesto);
        return turnos.stream().map(turnoInputMapper::toResponse).toList();
    }

    @POST
    @Path("/crear")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(@Valid CrearTurnoRequestDTO dto) {
        Turno turno = turnoCommandInputPort.crear(dto.getIdSucursal(), dto.getIdCola(), dto.getIdDetalle(), dto.getIdPersona(), dto.getTipoCasoEspecial());
        turnoWebSocket.enviarTurno("{\"event\":\"TURNO_CREADO\",\"idSucursal\":" + dto.getIdSucursal() + "}");
        return Response.status(Response.Status.CREATED)
                .entity(turnoInputMapper.toResponse(turno)).build();
    }

    @PUT
    @Path("/llamar-siguiente")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response llamarSiguiente(@Valid LlamarSiguienteTurnoRequestDTO dto) {
        Turno turno = turnoCommandInputPort.llamarSiguiente(
                dto.getIdSucursal(), dto.getIdPuesto(), dto.getIdSucursalPuesto(), dto.getIdUsuario());
        turnoWebSocket.enviarTurno("{\"event\":\"TURNO_LLAMADO\",\"idSucursal\":" + dto.getIdSucursal() + "}");
        return Response.ok(turnoInputMapper.toResponse(turno)).build();
    }

    @PUT
    @Path("/{idSucursal}/{codigoTurno}/llamar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response llamar(
            @PathParam("idSucursal") Long idSucursal,
            @PathParam("codigoTurno") String codigoTurno,
            @QueryParam("fechaCreacion") String fechaCreacionStr,
            @Valid LlamarTurnoRequestDTO dto) {
        LocalDateTime fechaCreacion = LocalDateTime.parse(fechaCreacionStr);
        Turno turno = turnoCommandInputPort.llamar(idSucursal, fechaCreacion, codigoTurno,
                dto.getIdPuesto(), dto.getIdSucursalPuesto(), dto.getIdUsuario());
        turnoWebSocket.enviarTurno("{\"event\":\"TURNO_LLAMADO\",\"idSucursal\":" + idSucursal + "}");
        return Response.ok(turnoInputMapper.toResponse(turno)).build();
    }

    @POST
    @Path("/{idSucursal}/{codigoTurno}/reasignar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reasignar(
            @PathParam("idSucursal") Long idSucursal,
            @PathParam("codigoTurno") String codigoTurno,
            @QueryParam("fechaCreacion") String fechaCreacionStr,
            @Valid ReasignarTurnoRequestDTO dto) {
        LocalDateTime fechaCreacion = LocalDateTime.parse(fechaCreacionStr);
        Turno nuevoTurno = turnoCommandInputPort.reasignar(idSucursal, fechaCreacion, codigoTurno,
                dto.getIdSucursalDestino(), dto.getIdColaDestino(), dto.getIdDetalleDestino());
        return Response.status(Response.Status.CREATED)
                .entity(turnoInputMapper.toResponse(nuevoTurno)).build();
    }

    @PUT
    @Path("/{idSucursal}/{codigoTurno}/finalizar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response finalizar(
            @PathParam("idSucursal") Long idSucursal,
            @PathParam("codigoTurno") String codigoTurno,
            @QueryParam("fechaCreacion") String fechaCreacionStr) {
        LocalDateTime fechaCreacion = LocalDateTime.parse(fechaCreacionStr);
        Turno turno = turnoCommandInputPort.finalizar(idSucursal, fechaCreacion, codigoTurno);
        turnoWebSocket.enviarTurno("{\"event\":\"TURNO_FINALIZADO\",\"idSucursal\":" + idSucursal + "}");
        return Response.ok(turnoInputMapper.toResponse(turno)).build();
    }

    @PUT
    @Path("/{idSucursal}/{codigoTurno}/re-llamar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rellamar(
            @PathParam("idSucursal") Long idSucursal,
            @PathParam("codigoTurno") String codigoTurno,
            @QueryParam("fechaCreacion") String fechaCreacionStr,
            @Valid LlamarTurnoRequestDTO dto) {
        LocalDateTime fechaCreacion = LocalDateTime.parse(fechaCreacionStr);
        Turno turno = turnoCommandInputPort.rellamar(idSucursal, fechaCreacion, codigoTurno,
                dto.getIdPuesto(), dto.getIdSucursalPuesto(), dto.getIdUsuario());
        turnoWebSocket.enviarTurno("{\"event\":\"TURNO_LLAMADO\",\"idSucursal\":" + idSucursal + "}");
        return Response.ok(turnoInputMapper.toResponse(turno)).build();
    }
}
