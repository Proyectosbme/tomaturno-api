package com.empresa.tomaturno.framework.adapters.input.controller;

import com.empresa.tomaturno.empresa.application.command.port.input.EmpresaCommandInputPort;
import com.empresa.tomaturno.empresa.application.query.port.input.EmpresaQueryInputPort;
import com.empresa.tomaturno.empresa.dominio.entity.Empresa;
import com.empresa.tomaturno.framework.adapters.input.dto.EmpresaNombreRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.EmpresaInputMapper;

import io.quarkus.security.Authenticated;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/empresa")
public class EmpresaController {

    private final EmpresaCommandInputPort commandPort;
    private final EmpresaQueryInputPort queryPort;
    private final EmpresaInputMapper mapper;

    public EmpresaController(EmpresaCommandInputPort commandPort,
                              EmpresaQueryInputPort queryPort,
                              EmpresaInputMapper mapper) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.mapper = mapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtener() {
        Empresa empresa = queryPort.obtener();
        return Response.ok(mapper.toResponse(empresa)).build();
    }

    @PATCH
    @Path("/nombre")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarNombre(@Valid EmpresaNombreRequestDTO dto) {
        Empresa empresa = commandPort.actualizarNombre(dto.getNombre());
        return Response.ok(mapper.toResponse(empresa)).build();
    }

    @PATCH
    @Path("/banner")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response actualizarBanner(@RestForm("banner") FileUpload banner) throws java.io.IOException {
        byte[] bannerBytes = java.nio.file.Files.readAllBytes(banner.uploadedFile());
        Empresa empresa = commandPort.actualizarBanner(bannerBytes);
        return Response.ok(mapper.toResponse(empresa)).build();
    }

    @PATCH
    @Path("/logo")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response actualizarLogo(@RestForm("logo") FileUpload logo) throws java.io.IOException {
        byte[] logoBytes = java.nio.file.Files.readAllBytes(logo.uploadedFile());
        Empresa empresa = commandPort.actualizarLogo(logoBytes);
        return Response.ok(mapper.toResponse(empresa)).build();
    }
}
