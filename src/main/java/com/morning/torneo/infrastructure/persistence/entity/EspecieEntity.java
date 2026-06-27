package com.morning.torneo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "especies")
public class EspecieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(name = "nivel_poder", nullable = false)
    private int nivelPoder;

    @Column(name = "habilidad_especial", nullable = false, length = 100)
    private String habilidadEspecial;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public EspecieEntity() {
    }

    public EspecieEntity(String nombre, int nivelPoder, String habilidadEspecial, LocalDateTime fechaCreacion) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EspecieEntity that = (EspecieEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EspecieEntity{id=" + id + ", nombre='" + nombre + '\'' + ", nivelPoder=" + nivelPoder
                + ", habilidadEspecial='" + habilidadEspecial + '\'' + ", fechaCreacion=" + fechaCreacion + '}';
    }
}
