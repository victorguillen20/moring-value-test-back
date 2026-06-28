package com.morning.torneo.application.dto;

import java.time.LocalDateTime;

public class EspecieResponse {

    private Long id;
    private String nombre;
    private int nivelPoder;
    private String habilidadEspecial;
    private LocalDateTime fechaCreacion;

    public EspecieResponse() {
    }

    public EspecieResponse(Long id, String nombre, int nivelPoder, String habilidadEspecial, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.nivelPoder = nivelPoder;
        this.habilidadEspecial = habilidadEspecial;
        this.fechaCreacion = fechaCreacion;
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
}
