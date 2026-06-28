package com.morning.torneo.application.dto;

public class RankingResponse {

    private int posicion;
    private EspecieResponse especie;
    private int victorias;

    public RankingResponse() {
    }

    public RankingResponse(int posicion, EspecieResponse especie, int victorias) {
        this.posicion = posicion;
        this.especie = especie;
        this.victorias = victorias;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public EspecieResponse getEspecie() {
        return especie;
    }

    public void setEspecie(EspecieResponse especie) {
        this.especie = especie;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }
}
