package com.morning.torneo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "combates")
public class CombateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especie1_id", nullable = false)
    private EspecieEntity especie1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especie2_id", nullable = false)
    private EspecieEntity especie2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ganador_id", nullable = false)
    private EspecieEntity ganador;

    @Column(name = "nivel_efectivo_especie_1", nullable = false)
    private int nivelEfectivoEspecie1;

    @Column(name = "nivel_efectivo_especie_2", nullable = false)
    private int nivelEfectivoEspecie2;

    @Column(name = "modificador_especie_1", nullable = false)
    private int modificadorEspecie1;

    @Column(name = "modificador_especie_2", nullable = false)
    private int modificadorEspecie2;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    public CombateEntity() {
    }

    public CombateEntity(EspecieEntity especie1, EspecieEntity especie2, EspecieEntity ganador,
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

    public EspecieEntity getEspecie1() {
        return especie1;
    }

    public void setEspecie1(EspecieEntity especie1) {
        this.especie1 = especie1;
    }

    public EspecieEntity getEspecie2() {
        return especie2;
    }

    public void setEspecie2(EspecieEntity especie2) {
        this.especie2 = especie2;
    }

    public EspecieEntity getGanador() {
        return ganador;
    }

    public void setGanador(EspecieEntity ganador) {
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
        CombateEntity that = (CombateEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CombateEntity{id=" + id + ", especie1=" + especie1 + ", especie2=" + especie2
                + ", ganador=" + ganador + ", nivelEfectivoEspecie1=" + nivelEfectivoEspecie1
                + ", nivelEfectivoEspecie2=" + nivelEfectivoEspecie2
                + ", modificadorEspecie1=" + modificadorEspecie1
                + ", modificadorEspecie2=" + modificadorEspecie2 + ", fecha=" + fecha + '}';
    }
}
