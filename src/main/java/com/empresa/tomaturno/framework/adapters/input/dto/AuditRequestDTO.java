package com.empresa.tomaturno.framework.adapters.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Base para todos los DTOs de creación/actualización.
 * Centraliza el campo "usuario" para facilitar la migración a Keycloak:
 * cuando se integre, este campo se podrá poblar automáticamente desde el token
 * sin tocar cada DTO individual.
 */
@Getter
@Setter
public abstract class AuditRequestDTO {

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 2, max = 100, message = "El usuario debe tener entre 2 y 100 caracteres")
    private String usuario;
}
