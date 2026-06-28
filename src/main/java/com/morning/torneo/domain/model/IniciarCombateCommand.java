package com.morning.torneo.domain.model;

public class IniciarCombateCommand {

    private Long especie1Id;
    private Long especie2Id;
    private boolean esDesempate;

    public IniciarCombateCommand() {
        this.esDesempate = false;
    }

    public IniciarCombateCommand(Long especie1Id, Long especie2Id, boolean esDesempate) {
        this.especie1Id = especie1Id;
        this.especie2Id = especie2Id;
        this.esDesempate = esDesempate;
    }

    public Long getEspecie1Id() {
        return especie1Id;
    }

    public void setEspecie1Id(Long especie1Id) {
        this.especie1Id = especie1Id;
    }

    public Long getEspecie2Id() {
        return especie2Id;
    }

    public void setEspecie2Id(Long especie2Id) {
        this.especie2Id = especie2Id;
    }

    public boolean isDesempate() {
        return esDesempate;
    }

    public void setDesempate(boolean esDesempate) {
        this.esDesempate = esDesempate;
    }
}
