package com.nexusdesk.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    private String login;
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String login, String password) {
        this.login = login;
        this.username = login;
        this.password = password;
    }

    public String getLogin() {
        if (login != null && !login.trim().isEmpty()) {
            return login.trim();
        }
        return username != null ? username.trim() : "";
    }
    public void setLogin(String login) { this.login = login; }
    public void setUsuario(String usuario) { this.login = usuario; }
    public String getUsuario() { return getLogin(); }

    public String getUsername() {
        if (username != null && !username.trim().isEmpty()) {
            return username.trim();
        }
        return login != null ? login.trim() : "";
    }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
