package com.empresa.tomaturno.cola.DTO;

import java.util.List;

public class ResultadoReplicacion {
    private final List<String> colasCopidas;
    private final List<String> colasSaltadas;
    private final List<String> detallesCopiados;

    public ResultadoReplicacion(List<String> colasCopidas, List<String> colasSaltadas, List<String> detallesCopiados) {
        this.colasCopidas = colasCopidas;
        this.colasSaltadas = colasSaltadas;
        this.detallesCopiados = detallesCopiados;
    }

    public int getTotalCopiadas() {
        return colasCopidas.size();
    }

    public int getTotalSaltadas() {
        return colasSaltadas.size();
    }

    public List<String> getColasCopidas() {
        return colasCopidas;
    }

    public List<String> getColasSaltadas() {
        return colasSaltadas;
    }

    public List<String> getDetallesCopiados() {
        return detallesCopiados;
    }

}
