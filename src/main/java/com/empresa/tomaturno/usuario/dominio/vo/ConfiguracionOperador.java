package com.empresa.tomaturno.usuario.dominio.vo;

import com.empresa.tomaturno.usuario.dominio.exceptions.UsuarioValidationException;

public class ConfiguracionOperador {

    private  String perfil;
    private final String ip;
    private final Integer correlativoPuesto;
    private final Integer atenderCasosEspeciales;

    private ConfiguracionOperador(String perfil, String ip, Integer correlativoPuesto, Integer atenderCasosEspeciales) {
        this.perfil = perfil;
        this.ip = ip;
        this.correlativoPuesto = correlativoPuesto;
        this.atenderCasosEspeciales = atenderCasosEspeciales == null ? 0 : atenderCasosEspeciales;
    }

    public static ConfiguracionOperador crear(String perfil, String ip, Integer correlativo,
            Integer atenderCasosEspeciales) {
        if (perfil == null || perfil.isBlank())
            throw new UsuarioValidationException("El perfil es obligatorio");
        if ("OPERADOR".equals(perfil) && correlativo == null)
            throw new UsuarioValidationException("El número de estación es obligatorio para el perfil Operador");
        return new ConfiguracionOperador(perfil, ip, correlativo, atenderCasosEspeciales);
    }

    public static ConfiguracionOperador reconstituir(String perfil, String ip, Integer correlativo,
            Integer atenderCasosEspeciales) {
        return new ConfiguracionOperador(perfil, ip, correlativo, atenderCasosEspeciales);
    }

    public void asignarPerfil(String perfil) {
        if (perfil == null || perfil.isBlank())
            throw new UsuarioValidationException("El perfil es obligatorio");
        this.perfil = perfil;
    }
    public String getPerfil() {
        return perfil;
    }

    public String getIp() {
        return ip;
    }

    public Integer getCorrelativoPuesto() {
        return correlativoPuesto;
    }

    public Integer getAtenderCasosEspeciales() {
        return atenderCasosEspeciales;
    }
}
