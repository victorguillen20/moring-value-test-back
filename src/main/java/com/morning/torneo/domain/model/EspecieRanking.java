package com.morning.torneo.domain.model;

import java.util.Objects;

public class EspecieRanking {

    private Especie especie;
    private int victorias;

    public EspecieRanking() {
    }

    public EspecieRanking(Especie especie, int victorias) {
        this.especie = especie;
        this.victorias = victorias;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EspecieRanking that = (EspecieRanking) o;
        return especie != null && that.especie != null
                && Objects.equals(especie.getId(), that.especie.getId());
    }

    @Override
    public int hashCode() {
        return especie != null && especie.getId() != null
                ? Objects.hash(especie.getId())
                : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EspecieRanking{especie=" + especie + ", victorias=" + victorias + '}';
    }
}
