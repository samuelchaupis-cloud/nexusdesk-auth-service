package com.nexusdesk.auth.dto;

public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String usuario;
    private String nombre;
    private String email;
    private String rol;
    private String cargo;
    private String avatarColor;
    private Long clienteId;

    public LoginResponse() {}

    public LoginResponse(String token, Long id, String usuario, String nombre, String email, String rol, String cargo, String avatarColor, Long clienteId) {
        this.token = token;
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.cargo = cargo;
        this.avatarColor = avatarColor;
        this.clienteId = clienteId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getAvatarColor() { return avatarColor; }
    public void setAvatarColor(String avatarColor) { this.avatarColor = avatarColor; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getUsername() { return usuario; }
    public String getNombreCompleto() { return nombre; }
}
