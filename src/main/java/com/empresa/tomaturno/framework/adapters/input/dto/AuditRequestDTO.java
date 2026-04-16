package com.empresa.tomaturno.framework.adapters.input.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Base para todos los DTOs de creación/actualización.
 * El campo "usuario" ya no se recibe del frontend — se obtiene del JWT de Keycloak en el controller.
 */
@Getter
@Setter
public abstract class AuditRequestDTO {
}
