package com.empresa.tomaturno.framework.adapters.input.controller;

import com.empresa.tomaturno.framework.adapters.input.dto.PersonaRequestDTO;
import com.empresa.tomaturno.framework.adapters.input.dto.PersonaResponseDTO;
import com.empresa.tomaturno.framework.adapters.input.mapper.PersonaInputMapper;
import com.empresa.tomaturno.persona.application.command.port.input.PersonaCommandInputPort;
import com.empresa.tomaturno.persona.dominio.entity.Persona;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestForm;

@Path("/personas")
public class PersonaController {

    private final PersonaCommandInputPort personaCommandInputPort;
    private final PersonaInputMapper personaInputMapper;

    public PersonaController(PersonaCommandInputPort personaCommandInputPort,
            PersonaInputMapper personaInputMapper) {
        this.personaCommandInputPort = personaCommandInputPort;
        this.personaInputMapper = personaInputMapper;
    }

    @POST
    @Path("/crear-o-actualizar")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearOActualizar(@Valid PersonaRequestDTO dto) {
        Persona persona = personaInputMapper.toDomain(dto);
        persona = personaCommandInputPort.crearOActualizar(persona);
        PersonaResponseDTO response = personaInputMapper.toResponse(persona);
        return Response.ok(response).build();
    }

    @PATCH
    @Path("/{id}/foto")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response asignarFoto(@PathParam("id") Long id, @RestForm("foto") byte[] foto) {
        Persona persona = personaCommandInputPort.asignarFoto(id, foto);
        PersonaResponseDTO response = personaInputMapper.toResponse(persona);
        return Response.ok(response).build();
    }
}
