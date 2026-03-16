package com.coop.tomaturno.framework.adapters.exceptions;

import com.coop.tomaturno.cola.dominio.exceptions.ColaNotFoundException;
import com.coop.tomaturno.cola.dominio.exceptions.ColaValidationException;
import com.coop.tomaturno.sucursal.dominio.exceptions.SucursalNotFoundException;
import com.coop.tomaturno.sucursal.dominio.exceptions.SucursalValidationException;
import com.coop.tomaturno.turno.dominio.exceptions.TurnoNotFoundException;
import com.coop.tomaturno.turno.dominio.exceptions.TurnoValidationException;

import jakarta.json.bind.JsonbException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

public class GlobalExceptionHandler {

    // ══════════════════════════════════════════════════
    // DOMINIO — Cola
    // ══════════════════════════════════════════════════

    @Provider
    public static class ColaValidationExceptionMapper implements ExceptionMapper<ColaValidationException> {
        @Override
        public Response toResponse(ColaValidationException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    400, "Validación de Cola", exception.getMessage(), "/colas");
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    @Provider
    public static class ColaNotFoundExceptionMapper implements ExceptionMapper<ColaNotFoundException> {
        @Override
        public Response toResponse(ColaNotFoundException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    404, "Cola No Encontrada", exception.getMessage(), "/colas");
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // DOMINIO — Sucursal
    // ══════════════════════════════════════════════════

    @Provider
    public static class SucursalValidationExceptionMapper implements ExceptionMapper<SucursalValidationException> {
        @Override
        public Response toResponse(SucursalValidationException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    400, "Validación de Sucursal", exception.getMessage(), "/sucursales");
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    @Provider
    public static class SucursalNotFoundExceptionMapper implements ExceptionMapper<SucursalNotFoundException> {
        @Override
        public Response toResponse(SucursalNotFoundException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    404, "Sucursal No Encontrada", exception.getMessage(), "/sucursales");
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // DOMINIO — Turno
    // ══════════════════════════════════════════════════

    @Provider
    public static class TurnoValidationExceptionMapper implements ExceptionMapper<TurnoValidationException> {
        @Override
        public Response toResponse(TurnoValidationException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    409, "Operación no permitida", exception.getMessage(), "/turnos");
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    @Provider
    public static class TurnoNotFoundExceptionMapper implements ExceptionMapper<TurnoNotFoundException> {
        @Override
        public Response toResponse(TurnoNotFoundException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    404, "Turno No Encontrado", exception.getMessage(), "/turnos");
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // FRAMEWORK — Not Found genérico
    // ══════════════════════════════════════════════════

    @Provider
    public static class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
        @Override
        public Response toResponse(NotFoundException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    404, "Recurso No Encontrado", exception.getMessage(), "/api");
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // VALIDACIONES — @Valid, @NotNull, @NotBlank, etc.
    // Muestra campo + mensaje + valor recibido
    // ══════════════════════════════════════════════════

    @Provider
    public static class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
        @Override
        public Response toResponse(ConstraintViolationException exception) {
            List<String> detalles = exception.getConstraintViolations()
                    .stream()
                    .map(v -> {
                        String path = v.getPropertyPath().toString();
                        String campo = path.contains(".")
                                ? path.substring(path.lastIndexOf('.') + 1)
                                : path;
                        return "'" + campo + "': " + v.getMessage()
                                + " (valor recibido: " + formatearValor(v) + ")";
                    })
                    .sorted()
                    .toList();

            ErrorResponseDTO error = new ErrorResponseDTO(
                    400,
                    "Validación de Datos",
                    "Uno o más campos no cumplen con las validaciones requeridas",
                    "/api",
                    detalles);

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }

        private String formatearValor(ConstraintViolation<?> v) {
            Object val = v.getInvalidValue();
            if (val == null) return "null";
            String str = val.toString();
            return str.isBlank() ? "(vacío)" : "\"" + str + "\"";
        }
    }

    // ══════════════════════════════════════════════════
    // JSON-B — tipo de dato incorrecto o JSON malformado
    // Ej: { "idSucursal": "abc" } cuando espera Long
    // Yasson lanza JsonbException directamente
    // ══════════════════════════════════════════════════

    @Provider
    public static class JsonbExceptionMapper implements ExceptionMapper<JsonbException> {
        @Override
        public Response toResponse(JsonbException exception) {
            String mensaje = exception.getMessage() != null
                    ? exception.getMessage().split("\n")[0]
                    : "Error de deserialización";

            ErrorResponseDTO error = new ErrorResponseDTO(
                    400,
                    "JSON Inválido",
                    "El cuerpo de la solicitud contiene un tipo de dato incorrecto o un campo inválido. "
                    + "Verifique que los campos numéricos  no contengan texto.",
                    "/api",
                    List.of(mensaje));

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // WebApplicationException — cubre BadRequestException
    // y otras excepciones JAX-RS. Inspecciona la causa
    // por si JSON-B está envuelto adentro.
    // ══════════════════════════════════════════════════

    @Provider
    public static class WebApplicationExceptionMapper implements ExceptionMapper<jakarta.ws.rs.WebApplicationException> {
        @Override
        public Response toResponse(jakarta.ws.rs.WebApplicationException exception) {
            // Busca JsonbException en la cadena de causas
            Throwable causa = exception.getCause();
            while (causa != null) {
                if (causa instanceof JsonbException) {
                    String detalle = causa.getMessage() != null
                            ? causa.getMessage().split("\n")[0]
                            : "Tipo de dato inválido";

                    ErrorResponseDTO error = new ErrorResponseDTO(
                            400,
                            "JSON Inválido",
                            "El cuerpo de la solicitud contiene un tipo de dato incorrecto. "
                            + "Verifique que los campos numéricos  no contengan texto.",
                            "/api",
                            List.of(detalle));

                    return Response.status(Response.Status.BAD_REQUEST)
                            .type(MediaType.APPLICATION_JSON)
                            .entity(error).build();
                }
                causa = causa.getCause();
            }

            // Otros WebApplicationException
            int status = exception.getResponse().getStatus();
            ErrorResponseDTO error = new ErrorResponseDTO(
                    status,
                    "Error en la Solicitud",
                    exception.getMessage() != null ? exception.getMessage() : "Error procesando la solicitud",
                    "/api");

            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // PARÁMETRO INVÁLIDO — query params requeridos faltantes
    // ══════════════════════════════════════════════════

    @Provider
    public static class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
        @Override
        public Response toResponse(IllegalArgumentException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    400,
                    "Parámetro Inválido",
                    exception.getMessage(),
                    "/api");
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // TIPO DE DATO — NumberFormatException
    // Ej: query param que espera número recibe texto
    // ══════════════════════════════════════════════════

    @Provider
    public static class NumberFormatExceptionMapper implements ExceptionMapper<NumberFormatException> {
        @Override
        public Response toResponse(NumberFormatException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    400,
                    "Tipo de Dato Inválido",
                    "Se esperaba un número pero se recibió un valor no numérico: " + exception.getMessage(),
                    "/api");
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // ERROR INTERNO CONTROLADO — InternalServerException
    // ══════════════════════════════════════════════════

    @Provider
    public static class InternalServerExceptionMapper implements ExceptionMapper<InternalServerException> {
        @Override
        public Response toResponse(InternalServerException exception) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    500,
                    "Error Interno del Servidor",
                    exception.getMessage(),
                    "/api");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }

    // ══════════════════════════════════════════════════
    // GENÉRICO — último recurso, captura todo lo no manejado
    // Inspecciona la cadena de causas buscando JsonbException
    // ══════════════════════════════════════════════════

    @Provider
    public static class GenericExceptionMapper implements ExceptionMapper<Exception> {
        @Override
        public Response toResponse(Exception exception) {
            exception.printStackTrace();

            // Recorre la cadena de causas buscando JsonbException
            Throwable causa = exception.getCause();
            while (causa != null) {
                if (causa instanceof JsonbException) {
                    ErrorResponseDTO error = new ErrorResponseDTO(
                            400,
                            "JSON Inválido",
                            "El JSON enviado contiene tipos de datos incorrectos. "
                            + "Verifique campos numéricos ",
                            "/api",
                            List.of(causa.getMessage().split("\n")[0]));
                    return Response.status(Response.Status.BAD_REQUEST)
                            .type(MediaType.APPLICATION_JSON)
                            .entity(error).build();
                }
                causa = causa.getCause();
            }

            ErrorResponseDTO error = new ErrorResponseDTO(
                    500,
                    "Error Interno del Servidor",
                    "Ocurrió un error inesperado. Por favor contacte al administrador del sistema.",
                    "/api");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error).build();
        }
    }
}