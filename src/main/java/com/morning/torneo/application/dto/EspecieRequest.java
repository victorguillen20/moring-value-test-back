package com.morning.torneo.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EspecieRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String nombre;

    @Min(1)
    @Max(1000)
    private int nivelPoder;

    @NotBlank
    @Size(min = 1, max = 100)
    private String habilidadEspecial;

    public EspecieRequest() {
    }

    public EspecieRequest(String nombre, int nivelPoder, String habilidadEspecial) {
        this.nombre = nombre;
        this.nivelPoder = nivelPoder;
        this.habilidadEspecial = habilidadEspecial;
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
}
