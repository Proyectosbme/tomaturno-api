package com.empresa.tomaturno.framework.adapters.input.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

import com.empresa.tomaturno.cola.application.command.dto.ResultadoReplicacion;
import com.empresa.tomaturno.cola.application.command.port.input.ColaCommandInputPort;

import com.empresa.tomaturno.cola.application.query.port.input.ColaQueryInputPort;
import com.empresa.tomaturno.cola.dominio.entity.Cola;
import com.empresa.tomaturno.cola.dominio.entity.Detalle;
import com.empresa.tomaturno.cola.dominio.vo.Auditoria;
import com.empresa.tomaturno.cola.dominio.vo.Estado;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.ColaResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.DetalleRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.ColaInputMapper;

import io.quarkus.security.Authenticated;

@Path("/colas")
@Authenticated
public class ColaController {

    private static final String USUARIO_DEFAULT = "sistema";

    private final ColaCommandInputPort colaCommandInputPort;
    private final ColaQueryInputPort colaQueryInputPort;
    private final ColaInputMapper colaInputMapper;
    private final TurnoWebSocket turnoWebSocket;

    @Context
    SecurityContext securityContext;

    public ColaController(ColaCommandInputPort colaCommandInputPort,
            ColaQueryInputPort colaQueryInputPort,
            ColaInputMapper colaInputMapper,
            TurnoWebSocket turnoWebSocket) {
        this.colaCommandInputPort = colaCommandInputPort;
        this.colaQueryInputPort = colaQueryInputPort;
        this.colaInputMapper = colaInputMapper;
        this.turnoWebSocket = turnoWebSocket;
    }

    private String usuarioActual() {
        return securityContext != null && securityContext.getUserPrincipal() != null
                ? securityContext.getUserPrincipal().getName()
                : USUARIO_DEFAULT;
    }

    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public List<ColaResponseDTO> buscarColasPorFiltro(
            @QueryParam("id") Long id,
            @QueryParam("idSucursal") Long idSucursal,
            @QueryParam("nombre") String nombre) {
        List<Cola> lstColas = colaQueryInputPort.buscarPorFiltro(id, idSucursal, nombre);
        if (turnoWebSocket != null) {
            turnoWebSocket.enviarTurno("Se ha buscado una cola con filtro: id=" + id + ", idSucursal=" + idSucursal
                    + ", nombre=" + nombre);
        }
        return lstColas.stream().map(colaInputMapper::toResponse).toList();
    }

    @GET
    @Path("/buscarConDetalles")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ColaResponseDTO> buscarColasConDetallePorFiltro(
            @QueryParam("idSucursal") Long idSucursal) {
        List<Cola> lstColas = colaQueryInputPort.buscarColasConDetalle(idSucursal);
        if (turnoWebSocket != null) {
            turnoWebSocket.enviarTurno("Se ha buscado una cola con filtro: idSucursal=" + idSucursal);
        }
        return lstColas.stream().map(colaInputMapper::toResponse).toList();
    }

    @GET
    @Path("/{idCola}/sucursal/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarColaConDetalles(
            @PathParam("idCola") Long idCola,
            @PathParam("idSucursal") Long idSucursal) {
        Cola cola = colaQueryInputPort.buscarConDetalles(idCola, idSucursal);
        return Response.ok(colaInputMapper.toResponse(cola)).build();
    }

    @POST
    @Path("/crear")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMIN", "SUBADMIN" })
    public Response crearCola(@Valid ColaRequestDTO colaRequestDTO) {
        Cola cola = colaInputMapper.toDomain(colaRequestDTO, usuarioActual());
        cola = colaCommandInputPort.crear(cola);
        return Response.status(Response.Status.CREATED)
                .entity(colaInputMapper.toResponse(cola)).build();
    }

    @PUT
    @Path("/{idCola}/sucursal/{idSucursal}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMIN", "SUBADMIN" })
    public Response modificarCola(
            @PathParam("idCola") Long idCola,
            @PathParam("idSucursal") Long idSucursal,
            @Valid ColaRequestDTO colaRequestDTO) {
        Auditoria auditoriaModificacion = Auditoria.of(usuarioActual(), java.time.LocalDateTime.now());
        Cola colaModificada = colaCommandInputPort.actualizar(idCola, idSucursal,
                colaRequestDTO.getNombre(), colaRequestDTO.getCodigo(), Estado.fromCodigo(colaRequestDTO.getEstado()),
                auditoriaModificacion);
        return Response.ok(colaInputMapper.toResponse(colaModificada)).build();
    }

    @POST
    @Path("/{idCola}/sucursal/{idSucursal}/detalles")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMIN", "SUBADMIN" })
    public Response crearDetalle(
            @PathParam("idCola") Long idCola,
            @PathParam("idSucursal") Long idSucursal,
            @Valid DetalleRequestDTO request) {
        Detalle.Builder detalleBuilder = colaInputMapper.toDetalleBuilder(request);
        Auditoria auditoriaCreacion = Auditoria.of(usuarioActual(), java.time.LocalDateTime.now());
        Cola cola = colaCommandInputPort.crearDetalle(idCola, idSucursal, detalleBuilder, auditoriaCreacion);
        return Response.status(Response.Status.CREATED)
                .entity(colaInputMapper.toResponse(cola)).build();
    }

    @PUT
    @Path("/{idCola}/sucursal/{idSucursal}/detalles/{idDetalle}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMIN", "SUBADMIN" })
    public Response modificarDetalle(
            @PathParam("idCola") Long idCola,
            @PathParam("idSucursal") Long idSucursal,
            @PathParam("idDetalle") Long idDetalle,
            @Valid DetalleRequestDTO request) {
        Auditoria auditoriaModificacion = Auditoria.of(usuarioActual(), java.time.LocalDateTime.now());
        Cola cola = colaCommandInputPort.editarDetalleCola(idCola, idSucursal, idDetalle,
                request.getNombre(), request.getCodigo(), Estado.fromCodigo(request.getEstado()),
                auditoriaModificacion);
        return Response.status(Response.Status.CREATED)
                .entity(colaInputMapper.toResponse(cola)).build();
    }

    @POST
    @Path("/replicar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMIN", "SUBADMIN" })
    public Response replicarColas(
            @QueryParam("idOrigen") Long idOrigen,
            @QueryParam("idDestino") Long idDestino) {
        if (idOrigen == null || idDestino == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Los parámetros idOrigen e idDestino son obligatorios").build();
        }
        if (idOrigen.equals(idDestino)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La sucursal origen y destino no pueden ser la misma").build();
        }

        ResultadoReplicacion resultado = colaCommandInputPort.replicar(idOrigen, idDestino, usuarioActual());

        return Response.ok(colaInputMapper.toReplicarResponse(resultado)).build();
    }
}
