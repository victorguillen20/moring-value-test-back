package com.morning.torneo.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario {

    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    public Usuario(String username, String email, String passwordHash, LocalDateTime fechaRegistro) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id != null && Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", username='" + username + '\'' + ", email='" + email + '\''
                + ", passwordHash='" + passwordHash + '\'' + ", fechaRegistro=" + fechaRegistro + '}';
    }
}
