package com.empresa.tomaturno.framework.adapters.input.controller;

import com.empresa.tomaturno.framework.adapters.output.jasper.ReporteService;
import com.empresa.tomaturno.shared.clases.FormatoRpt;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.HashMap;
import java.util.Map;

@Path("/reportes")
public class ReporteResource {

    private final ReporteService reporteService;

    public ReporteResource(ReporteService reporteService) {
        this.reporteService = reporteService;
    }


    @GET
    @Path("/{nombreReporte}")
    public Response generarReporte(
            @PathParam("nombreReporte") String nombreReporte,
            @QueryParam("formato") @DefaultValue("PDF") String formatoParam,
            @Context UriInfo uriInfo) {
        try {
            FormatoRpt formato = FormatoRpt.fromNombre(formatoParam);

            Map<String, Object> params = new HashMap<>();
            uriInfo.getQueryParameters().forEach((clave, valores) -> {
                if (!clave.equalsIgnoreCase("formato"))
                    params.put(clave, valores.get(0));
            });

            byte[] bytes = reporteService.generarReporte(nombreReporte, params, formato);

            return Response.ok(bytes)
                    .header("Content-Type", formato.getContentType())
                    .header("Content-Disposition", "inline; filename=" + nombreReporte + "." + formato.getExtension())
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato inválido. Use: PDF o EXCEL")
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
            FormatoRpt formato = FormatoRpt.fromNombre("PDF");
            Map<String, Object> params = new HashMap<>();
            params.put("idSucursal", idsucursal);
            params.put("usuario", "bmarroquin");


             byte[] bytes = reporteService.generarReporte("RptSucursal", params, formato);

            return Response.ok(bytes)
                    .header("Content-Type", formato.getContentType())
                    .header("Content-Disposition", "inline; filename=Reporte_Atencion_Sucursal_" + idsucursal + "." + formato.getExtension())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error generando reporte: " + e.getMessage())
                    .build();
        }
    }
}
