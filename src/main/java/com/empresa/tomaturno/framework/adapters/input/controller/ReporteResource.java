package com.empresa.tomaturno.framework.adapters.input.controller;

import com.empresa.tomaturno.framework.adapters.output.jasper.ReporteService;
import com.empresa.tomaturno.shared.clases.FormatoRpt;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Path("/reportes")
public class ReporteResource {

    private static final String USUARIO_DEFAULT = "sistema";
    private final ReporteService reporteService;

    @Context
    SecurityContext securityContext;

    public ReporteResource(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GET
    @Path("hs/{idSucursal}/{fechaini}/{fechafin}")
    public Response generarReporteHistorico(
            @PathParam("idSucursal") Integer idsucursal,
            @PathParam("fechaini") String fechaini,
            @PathParam("fechafin") String fechafin) {
        try {
            java.sql.Date fechaIniDate = parsearFecha(fechaini);
            java.sql.Date fechaFinDate = parsearFecha(fechafin);

            if (fechaIniDate == null || fechaFinDate == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato de fecha inválido. Use: yyyy-MM-dd")
                        .build();
            }

            if (fechaFinDate.before(fechaIniDate)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("La fecha fin no puede ser anterior a la fecha inicio")
                        .build();
            }

            String usuarioActual = usuarioActual();
            FormatoRpt formato = FormatoRpt.fromNombre("PDF");
            Map<String, Object> params = new HashMap<>();
            params.put("idSucursal", idsucursal);
            params.put("fechaini", fechaIniDate);
            params.put("fechafin", fechaFinDate);
            params.put("usuario", usuarioActual);

            byte[] bytes = reporteService.generarReporte("RptSucursalHS", params, formato);

            return Response.ok(bytes)
                    .header("Content-Type", formato.getContentType())
                    .header("Content-Disposition",
                            "inline; filename=Reporte_Historico_Sucursal_" + idsucursal + "." + formato.getExtension())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error generando reporte: " + e.getMessage())
                    .build();
        }
    }

    // RptSucursalxuserHS
    @GET
    @Path("hs/usuario/{idSucursal}/{fechaini}/{fechafin}")
    public Response generarReportexusuarioHistorico(
            @PathParam("idSucursal") Integer idsucursal,
            @PathParam("fechaini") String fechaini,
            @PathParam("fechafin") String fechafin) {
        try {
            java.sql.Date fechaIniDate = parsearFecha(fechaini);
            java.sql.Date fechaFinDate = parsearFecha(fechafin);

            if (fechaIniDate == null || fechaFinDate == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Formato de fecha inválido. Use: yyyy-MM-dd")
                        .build();
            }

            if (fechaFinDate.before(fechaIniDate)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("La fecha fin no puede ser anterior a la fecha inicio")
                        .build();
            }

            String usuarioActual = usuarioActual();
            FormatoRpt formato = FormatoRpt.fromNombre("PDF");
            Map<String, Object> params = new HashMap<>();
            params.put("idSucursal", idsucursal);
            params.put("fechaini", fechaIniDate);
            params.put("fechafin", fechaFinDate);
            params.put("usuario", usuarioActual);

            byte[] bytes = reporteService.generarReporte("RptSucursalxuserHS", params, formato);

            return Response.ok(bytes)
                    .header("Content-Type", formato.getContentType())
                    .header("Content-Disposition",
                            "inline; filename=Reporte_Historico_Sucursal_" + idsucursal + "." + formato.getExtension())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error generando reporte: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("actual/porusuario/{idSucursal}")
    public Response generarReportePorUsuario(
            @PathParam("idSucursal") Integer idsucursal) {
        try {
            String usuarioActual = usuarioActual();
            FormatoRpt formato = FormatoRpt.fromNombre("PDF");
            Map<String, Object> params = new HashMap<>();
            params.put("idSucursal", idsucursal);
            params.put("usuario", usuarioActual);

            byte[] bytes = reporteService.generarReporte("RptSucursalxusuario", params, formato);

            return Response.ok(bytes)
                    .header("Content-Type", formato.getContentType())
                    .header("Content-Disposition",
                            "inline; filename=Reporte_Atencion_Sucursal_" + idsucursal + "." + formato.getExtension())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error generando reporte: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("actual/{idSucursal}")
    public Response generarReporte(
            @PathParam("idSucursal") Integer idsucursal) {
        try {
            String usuarioActual = usuarioActual();
            FormatoRpt formato = FormatoRpt.fromNombre("PDF");
            Map<String, Object> params = new HashMap<>();
            params.put("idSucursal", idsucursal);
            params.put("usuario", usuarioActual);

            byte[] bytes = reporteService.generarReporte("RptSucursal", params, formato);

            return Response.ok(bytes)
                    .header("Content-Type", formato.getContentType())
                    .header("Content-Disposition",
                            "inline; filename=Reporte_Atencion_Sucursal_" + idsucursal + "." + formato.getExtension())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error generando reporte: " + e.getMessage())
                    .build();
        }
    }

    private String usuarioActual() {
        return securityContext != null && securityContext.getUserPrincipal() != null
                ? securityContext.getUserPrincipal().getName()
                : USUARIO_DEFAULT;
    }

    private java.sql.Date parsearFecha(String fecha) {
        try {
            return java.sql.Date.valueOf(LocalDate.parse(fecha));
        } catch (java.time.format.DateTimeParseException e) {
            return null;
        }
    }
}
