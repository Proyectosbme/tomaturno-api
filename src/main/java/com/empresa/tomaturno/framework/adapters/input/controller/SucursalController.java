package com.empresa.tomaturno.framework.adapters.input.controller;

import java.util.List;

import com.empresa.tomaturno.framework.adapters.input.dto.SucursalRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.SucursalResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.SucursalInputMapper;
import com.empresa.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.empresa.tomaturno.sucursal.application.query.port.input.SucursalQueryInputPort;
import com.empresa.tomaturno.sucursal.dominio.entity.Sucursal;

import io.quarkus.security.Authenticated;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/sucursal")
@Tag(name = "Sucursal", description = "Operaciones para la gestión de sucursales")
public class SucursalController {

    private static final String USUARIO_DEFAULT = "sistema";

    private final SucursalCommandInputPort sucursalCommandInputPort;
    private final SucursalInputMapper sucursalInputMapper;
    private final TurnoWebSocket turnoWebSocket;
    private final SucursalQueryInputPort sucursalQueryInputPort;

    @Context
    SecurityContext securityContext;

    public SucursalController(SucursalCommandInputPort sucursalCommandInputPort,
            SucursalInputMapper sucursalInputMapper,
            TurnoWebSocket turnoWebSocket,
            SucursalQueryInputPort sucursalQueryInputPort) {
        this.sucursalCommandInputPort = sucursalCommandInputPort;
        this.sucursalInputMapper = sucursalInputMapper;
        this.turnoWebSocket = turnoWebSocket;
        this.sucursalQueryInputPort = sucursalQueryInputPort;
    }

    private String usuarioActual() {
        return securityContext != null && securityContext.getUserPrincipal() != null
                ? securityContext.getUserPrincipal().getName()
                : USUARIO_DEFAULT;
    }

    @POST
    @Path("/crear")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crear sucursal", description = "Registra una nueva sucursal en el sistema y crea su configuración por defecto")
    @APIResponse(responseCode = "201", description = "Sucursal creada exitosamente")
    @APIResponse(responseCode = "400", description = "Datos de la sucursal inválidos")
    @RolesAllowed({"ADMIN"})
    public Response crearSucursal(@Valid SucursalRequestDTO sucursalRequestDTO) {
        String usuarioActual = usuarioActual();
        Sucursal sucursal = sucursalInputMapper.toSucursal(sucursalRequestDTO);
        Sucursal sucursalCreada = sucursalCommandInputPort.crear(sucursal, usuarioActual);
        if (turnoWebSocket != null) {
            turnoWebSocket.enviarTurno("Se ha creado una nueva sucursal");
        }
        SucursalResponseDTO responseDTO = sucursalInputMapper.toSucursalResponseDTO(sucursalCreada);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/modificar")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Modificar sucursal", description = "Actualiza los datos de una sucursal existente")
    @APIResponse(responseCode = "200", description = "Sucursal modificada exitosamente")
    @APIResponse(responseCode = "400", description = "Datos inválidos")
    @APIResponse(responseCode = "404", description = "Sucursal no encontrada")
    @RolesAllowed({"ADMIN"})
    public Response modificarSucursal(
            @Parameter(description = "ID de la sucursal a modificar", required = true) @QueryParam("id") Long id,
            @Valid SucursalRequestDTO sucursalRequestDTO) {
        String usuarioActual = usuarioActual();
        Sucursal sucursalDatosNuevos = sucursalInputMapper.toSucursal(sucursalRequestDTO);
        Sucursal sucursalModificada = sucursalCommandInputPort.actualizar(id, sucursalDatosNuevos, usuarioActual);
        SucursalResponseDTO responseDTO = sucursalInputMapper.toSucursalResponseDTO(sucursalModificada);
        return Response.ok(responseDTO).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Listar sucursales", description = "Retorna todas las sucursales registradas en el sistema")
    @APIResponse(responseCode = "200", description = "Lista de sucursales")
    public List<SucursalResponseDTO> listarTodas() {
        return sucursalQueryInputPort.listarTodas()
                .stream()
                .map(sucursalInputMapper::toSucursalResponseDTO)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener sucursal por ID", description = "Retorna una sucursal específica por su identificador")
    @APIResponse(responseCode = "200", description = "Sucursal encontrada")
    @APIResponse(responseCode = "404", description = "Sucursal no encontrada")
    @Authenticated
    public SucursalResponseDTO obtenerSucursalPorId(
            @Parameter(description = "ID de la sucursal", required = true) @PathParam("id") Long id) {
        Sucursal sucursal = sucursalQueryInputPort.buscarPorId(id);
        return sucursalInputMapper.toSucursalResponseDTO(sucursal);
    }

    @GET
    @Path("/sucursales")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Buscar sucursales por nombre", description = "Retorna las sucursales que coincidan con el nombre proporcionado")
    @APIResponse(responseCode = "200", description = "Lista de sucursales encontradas")
    @Authenticated
    public List<SucursalResponseDTO> obtenerSucursalesPorNombre(
            @Parameter(description = "Nombre o parte del nombre de la sucursal") @QueryParam("nombre") String nombre) {
        List<Sucursal> lstSucursales = sucursalQueryInputPort.buscarPorNombre(nombre);
        return lstSucursales.stream().map(sucursalInputMapper::toSucursalResponseDTO).toList();
    }
}
