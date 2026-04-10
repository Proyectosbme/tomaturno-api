package com.empresa.tomaturno.shared.clases;

public enum ConfiguracionClave {
    VALIDAR_IP,
    REINICIAR_NUMERACION,
    NUMERACION_POR_COLA_DETALLE,
    LLAMAR_CON_ACTIVO,
    ESCANEAR_DUI,
    CASOS_ESPECIALES;

    public String clave() {
        return this.name();
    }
}
