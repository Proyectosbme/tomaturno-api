package com.coop.tomaturno.framework.adapters.input.controller;



import java.util.List;

import com.coop.tomaturno.framework.adapters.input.dto.SucursalRequestDTO;
import com.coop.tomaturno.framework.adapters.input.dto.SucursalResponseDTO;
import com.coop.tomaturno.framework.adapters.input.mapper.SucursalInputMapper;
import com.coop.tomaturno.sucursal.application.command.port.input.SucursalCommandInputPort;
import com.coop.tomaturno.sucursal.application.query.port.input.SucursalQueryInputPort;
import com.coop.tomaturno.sucursal.dominio.entity.Sucursal;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sucursal")
public class SucursalController {

    private final SucursalCommandInputPort sucursalCommandInputPort;
    private final SucursalInputMapper sucursalInputMapper;
    private final TurnoWebSocket turnoWebSocket;
    private final SucursalQueryInputPort sucursalQueryInputPort;

    public SucursalController(SucursalCommandInputPort sucursalCommandInputPort,
                                     SucursalInputMapper sucursalInputMapper,
                                     TurnoWebSocket turnoWebSocket,
                                     SucursalQueryInputPort sucursalQueryInputPort) {
        this.sucursalCommandInputPort = sucursalCommandInputPort;
        this.sucursalInputMapper = sucursalInputMapper;
        this.turnoWebSocket = turnoWebSocket;
        this.sucursalQueryInputPort = sucursalQueryInputPort;
    }

    @POST
    @Path("/crear")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearSucursal(@Valid SucursalRequestDTO sucursalRequestDTO) {
        Sucursal sucursal = sucursalInputMapper.toSucursal(sucursalRequestDTO);
        Sucursal sucursalCreada = sucursalCommandInputPort.crear(sucursal);
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
    public Response modificarSucursal(@QueryParam("id") Long id,
                                      @Valid SucursalRequestDTO sucursalRequestDTO) {
        Sucursal sucursalDatosNuevos = sucursalInputMapper.toSucursal(sucursalRequestDTO);
        Sucursal sucursalModificada = sucursalCommandInputPort.actualizar(id, sucursalDatosNuevos);
        SucursalResponseDTO responseDTO = sucursalInputMapper.toSucursalResponseDTO(sucursalModificada);
        return Response.ok(responseDTO).build();
    }

     @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SucursalResponseDTO> listarTodas() {
        return sucursalQueryInputPort.listarTodas()
                .stream()
                .map(sucursalInputMapper::toSucursalResponseDTO)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SucursalResponseDTO obtenerSucursalPorId(@PathParam("id") Long id) {
        Sucursal sucursal = sucursalQueryInputPort.buscarPorId(id);
        return sucursalInputMapper.toSucursalResponseDTO(sucursal);
    }

    @GET
    @Path("/sucursales")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SucursalResponseDTO> obtenerSucursalesPorNombre(@QueryParam("nombre") String nombre) {
        List<Sucursal> lstSucursales = sucursalQueryInputPort.buscarPorNombre(nombre);
        return lstSucursales.stream().map(sucursalInputMapper::toSucursalResponseDTO).toList();
    }
}
