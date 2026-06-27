package com.morning.torneo.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Combate {

    private Long id;
    private Especie especie1;
    private Especie especie2;
    private Especie ganador;
    private int nivelEfectivoEspecie1;
    private int nivelEfectivoEspecie2;
    private int modificadorEspecie1;
    private int modificadorEspecie2;
    private LocalDateTime fecha;

    public Combate() {
    }

    public Combate(Especie especie1, Especie especie2, Especie ganador,
                   int nivelEfectivoEspecie1, int nivelEfectivoEspecie2,
                   int modificadorEspecie1, int modificadorEspecie2,
                   LocalDateTime fecha) {
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

    public Especie getEspecie1() {
        return especie1;
    }

    public void setEspecie1(Especie especie1) {
        this.especie1 = especie1;
    }

    public Especie getEspecie2() {
        return especie2;
    }

    public void setEspecie2(Especie especie2) {
        this.especie2 = especie2;
    }

    public Especie getGanador() {
        return ganador;
    }

    public void setGanador(Especie ganador) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Combate combate = (Combate) o;
        return id != null && Objects.equals(id, combate.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Combate{id=" + id + ", especie1=" + especie1 + ", especie2=" + especie2
                + ", ganador=" + ganador + ", nivelEfectivoEspecie1=" + nivelEfectivoEspecie1
                + ", nivelEfectivoEspecie2=" + nivelEfectivoEspecie2
                + ", modificadorEspecie1=" + modificadorEspecie1
                + ", modificadorEspecie2=" + modificadorEspecie2 + ", fecha=" + fecha + '}';
    }
}
