package com.morning.torneo.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Especie {

    private Long id;
    private String nombre;
    private int nivelPoder;
    private String habilidadEspecial;
    private LocalDateTime fechaCreacion;

    public Especie() {
    }

    public Especie(Long id, String nombre, int nivelPoder, String habilidadEspecial) {
        this.id = id;
        this.nombre = nombre;
        this.nivelPoder = nivelPoder;
        this.habilidadEspecial = habilidadEspecial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivelPoder() {
        return nivelPoder;
    }

    public void setNivelPoder(int nivelPoder) {
        this.nivelPoder = nivelPoder;
    }

    public String getHabilidadEspecial() {
        return habilidadEspecial;
    }

    public void setHabilidadEspecial(String habilidadEspecial) {
        this.habilidadEspecial = habilidadEspecial;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Especie especie = (Especie) o;
        return id != null && Objects.equals(id, especie.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Especie{id=" + id + ", nombre='" + nombre + '\'' + ", nivelPoder=" + nivelPoder
                + ", habilidadEspecial='" + habilidadEspecial + '\'' + ", fechaCreacion=" + fechaCreacion + '}';
    }
}
