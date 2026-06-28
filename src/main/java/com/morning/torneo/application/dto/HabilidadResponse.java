package com.morning.torneo.application.dto;

public class HabilidadResponse {

    private String nombre;
    private int modificador;

    public HabilidadResponse() {
    }

    public HabilidadResponse(String nombre, int modificador) {
        this.nombre = nombre;
        this.modificador = modificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getModificador() {
        return modificador;
    }

    public void setModificador(int modificador) {
        this.modificador = modificador;
    }
}
