package com.nexusdesk.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_backups")
public class BackupRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "tamano_legible", nullable = false, length = 50)
    private String tamanoLegible;

    @Column(name = "tamano_bytes", nullable = false)
    private Long tamanoBytes;

    @Column(name = "tablas_respaldadas", nullable = false)
    private Integer tablasRespaldadas;

    @Column(name = "filas_respaldadas", nullable = false)
    private Integer filasRespaldadas;

    @Column(name = "checksum_sha256", nullable = false, length = 64)
    private String checksumSha256;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "creado_por", nullable = false, length = 100)
    private String creadoPor;

    public BackupRecord() {
        this.fecha = LocalDateTime.now();
        this.estado = "COMPLETADO";
        this.creadoPor = "SYSTEM";
    }

    // Getters y Setters estándar
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getTamanoLegible() { return tamanoLegible; }
    public void setTamanoLegible(String tamanoLegible) { this.tamanoLegible = tamanoLegible; }
    public Long getTamanoBytes() { return tamanoBytes; }
    public void setTamanoBytes(Long tamanoBytes) { this.tamanoBytes = tamanoBytes; }
    public Integer getTablasRespaldadas() { return tablasRespaldadas; }
    public void setTablasRespaldadas(Integer tablasRespaldadas) { this.tablasRespaldadas = tablasRespaldadas; }
    public Integer getFilasRespaldadas() { return filasRespaldadas; }
    public void setFilasRespaldadas(Integer filasRespaldadas) { this.filasRespaldadas = filasRespaldadas; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
}
