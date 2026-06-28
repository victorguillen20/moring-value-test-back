package com.morning.torneo.application.dto;

public class AuthResponse {

    private String token;
    private String tipo;
    private String username;
    private long expiresIn;

    public AuthResponse() {
    }

    public AuthResponse(String token, String tipo, String username, long expiresIn) {
        this.token = token;
        this.tipo = tipo;
        this.username = username;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
