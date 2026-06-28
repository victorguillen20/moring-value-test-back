package com.morning.torneo.application.dto;

import java.time.LocalDateTime;

public class CombateResponse {

    private Long id;
    private EspecieResponse especie1;
    private EspecieResponse especie2;
    private EspecieResponse ganador;
    private int nivelEfectivoEspecie1;
    private int nivelEfectivoEspecie2;
    private int modificadorEspecie1;
    private int modificadorEspecie2;
    private LocalDateTime fecha;

    public CombateResponse() {
    }

    public CombateResponse(Long id, EspecieResponse especie1, EspecieResponse especie2,
                           EspecieResponse ganador, int nivelEfectivoEspecie1,
                           int nivelEfectivoEspecie2, int modificadorEspecie1,
                           int modificadorEspecie2, LocalDateTime fecha) {
        this.id = id;
        this.especie1 = especie1;
        this.especie2 = especie2;
        this.ganador = ganador;
        this.nivelEfectivoEspecie1 = nivelEfectivoEspecie1;
        this.nivelEfectivoEspecie2 = nivelEfectivoEspecie2;
        this.modificadorEspecie1 = modificadorEspecie1;
        this.modificadorEspecie2 = modificadorEspecie2;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EspecieResponse getEspecie1() {
        return especie1;
    }

    public void setEspecie1(EspecieResponse especie1) {
        this.especie1 = especie1;
    }

    public EspecieResponse getEspecie2() {
        return especie2;
    }

    public void setEspecie2(EspecieResponse especie2) {
        this.especie2 = especie2;
    }

    public EspecieResponse getGanador() {
        return ganador;
    }

    public void setGanador(EspecieResponse ganador) {
        this.ganador = ganador;
    }

    public int getNivelEfectivoEspecie1() {
        return nivelEfectivoEspecie1;
    }

    public void setNivelEfectivoEspecie1(int nivelEfectivoEspecie1) {
        this.nivelEfectivoEspecie1 = nivelEfectivoEspecie1;
    }

    public int getNivelEfectivoEspecie2() {
        return nivelEfectivoEspecie2;
    }

    public void setNivelEfectivoEspecie2(int nivelEfectivoEspecie2) {
        this.nivelEfectivoEspecie2 = nivelEfectivoEspecie2;
    }

    public int getModificadorEspecie1() {
        return modificadorEspecie1;
    }

    public void setModificadorEspecie1(int modificadorEspecie1) {
        this.modificadorEspecie1 = modificadorEspecie1;
    }

    public int getModificadorEspecie2() {
        return modificadorEspecie2;
    }

    public void setModificadorEspecie2(int modificadorEspecie2) {
        this.modificadorEspecie2 = modificadorEspecie2;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
