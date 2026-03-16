package com.coop.tomaturno.framework.adapters.input.controller;

import com.coop.tomaturno.framework.adapters.input.dto.PersonaRequestDTO;
import com.coop.tomaturno.framework.adapters.input.dto.PersonaResponseDTO;
import com.coop.tomaturno.framework.adapters.input.mapper.PersonaInputMapper;
import com.coop.tomaturno.persona.application.command.port.input.PersonaCommandInputPort;
import com.coop.tomaturno.persona.dominio.entity.Persona;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
}
