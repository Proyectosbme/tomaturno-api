package com.empresa.tomaturno.framework.adapters.output.jasper;

import com.empresa.tomaturno.shared.clases.FormatoRpt;
import jakarta.enterprise.context.ApplicationScoped;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ReporteService {

    private final DataSource dataSource;

    public ReporteService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] generarReporte(String nombreReporte, Map<String, Object> params, FormatoRpt formato) throws Exception {
        InputStream template = getClass().getResourceAsStream("/reportes/" + nombreReporte + ".jasper");

        if (template == null)
            throw new RuntimeException("No se encontró el archivo del reporte: " + nombreReporte);

        if (params == null)
            params = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            JasperPrint print = JasperFillManager.fillReport(template, params, conn);
            return switch (formato) {
                case PDF -> exportarPdf(print);
                case EXCEL -> exportarExcel(print);
            };
        }
    }

    private byte[] exportarPdf(JasperPrint print) throws Exception {
        return JasperExportManager.exportReportToPdf(print);
    }

    private byte[] exportarExcel(JasperPrint print) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setOnePagePerSheet(false);
        config.setRemoveEmptySpaceBetweenRows(true);
        exporter.setConfiguration(config);

        exporter.exportReport();
        return out.toByteArray();
    }
}
