package com.nexusdesk.auth.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AuditEventDto {

    private Long id;
    private Long usuarioId;
    private String usuario;
    private String accion;
    private String detalle;
    private String icono;
    private String color;
    private LocalDateTime createdAt;
    private String fecha;
    private String hace;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public AuditEventDto() {}

    public AuditEventDto(Long id, Long usuarioId, String usuario, String accion, String detalle,
                         String icono, String color, LocalDateTime createdAt) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuario = (usuario != null && !usuario.isBlank()) ? usuario : "Sistema";
        this.accion = accion;
        this.detalle = detalle;
        this.icono = (icono != null && !icono.isBlank()) ? icono : "bi-activity";
        this.color = (color != null && !color.isBlank()) ? color : "#4f46e5";
        this.createdAt = createdAt;
        if (createdAt != null) {
            this.fecha = createdAt.format(FORMATTER);
            this.hace = calculateHace(createdAt);
        } else {
            this.fecha = "—";
            this.hace = "—";
        }
    }

    private String calculateHace(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(date, now);
        if (minutes < 1) return "hace unos segundos";
        if (minutes < 60) return "hace " + minutes + " min";
        long hours = ChronoUnit.HOURS.between(date, now);
        if (hours < 24) return "hace " + hours + " h";
        long days = ChronoUnit.DAYS.between(date, now);
        if (days == 1) return "hace 1 d";
        return "hace " + days + " d";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        if (createdAt != null) {
            this.fecha = createdAt.format(FORMATTER);
            this.hace = calculateHace(createdAt);
        }
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHace() { return hace; }
    public void setHace(String hace) { this.hace = hace; }
}
