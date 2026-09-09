package com.empresa.tomaturno.shared.clases;

public enum ConfiguracionClave {
    VALIDAR_IP,
    LLAMAR_CON_ACTIVO,
    ESCANEAR_DUI,
    CASOS_ESPECIALES,
    TURNO_AUTOMATICO;

    public String clave() {
        return this.name();
    }
}
