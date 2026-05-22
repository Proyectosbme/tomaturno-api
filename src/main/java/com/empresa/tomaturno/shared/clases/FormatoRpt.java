package com.empresa.tomaturno.shared.clases;

public enum FormatoRpt {

    PDF("pdf", "application/pdf"),
    EXCEL("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final String extension;
    private final String contentType;

    FormatoRpt(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static FormatoRpt fromNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El formato no puede ser nulo o vacío");
        for (FormatoRpt f : values()) {
            if (f.name().equalsIgnoreCase(nombre.trim()))
                return f;
        }
        throw new IllegalArgumentException("Formato inválido: '" + nombre + "'. Valores válidos: PDF, EXCEL");
    }
}
