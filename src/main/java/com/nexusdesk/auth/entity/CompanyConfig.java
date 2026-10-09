package com.nexusdesk.auth.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "company_config")
public class CompanyConfig {

    @Id
    private Short id = 1;

    @Column(name = "razon_social", length = 150)
    private String razonSocial = "TECHDESK SOLUTIONS PERU S.A.C.";

    @Column(length = 100)
    private String eslogan = "Soporte TI de Misión Crítica & SLA 24/7";

    @Column(length = 80)
    private String ciudad = "Lima";

    @Column(length = 60)
    private String pais = "Perú";

    @Column(name = "horario_atencion", length = 120)
    private String horarioAtencion = "Lunes a Viernes 08:30 - 18:30";

    @Column(nullable = false, length = 150)
    private String nombre = "TechDesk Pro Cloud Enterprise";

    @Column(length = 15)
    private String ruc = "20601234565";

    @Column(name = "doc_etiqueta", length = 10)
    private String docEtiqueta = "RUC";

    @Column(length = 100)
    private String giro = "Servicios de tecnología e infraestructura";

    @Column(length = 200)
    private String direccion = "Av. Javier Prado Este 4567, San Isidro, Lima, Perú";

    @Column(length = 25)
    private String telefono = "+51 (01) 710-9000";

    @Column(length = 120)
    private String email = "contacto@techdesk.pe";

    @Column(length = 120)
    private String web = "https://techdesk.pe";

    @Column(name = "zona_horaria", length = 60)
    private String zonaHoraria = "America/Lima";

    @Column(name = "moneda_simbolo", nullable = false, length = 5)
    private String monedaSimbolo = "S/";

    @Column(name = "moneda_codigo", nullable = false, length = 5)
    private String monedaCodigo = "PEN";

    @Column(name = "moneda_pos", nullable = false, length = 10)
    private String monedaPos = "antes";

    @Column(name = "moneda_espacio", nullable = false)
    private Boolean monedaEspacio = true;

    @Column(name = "moneda_decimales", nullable = false)
    private Integer monedaDecimales = 2;

    @Column(name = "sep_decimal", nullable = false, length = 2)
    private String sepDecimal = ".";

    @Column(name = "sep_miles", nullable = false, length = 6)
    private String sepMiles = ",";

    @Column(name = "impuesto_nombre", length = 20)
    private String impuestoNombre = "IGV";

    @Column(name = "igv_pct", nullable = false, precision = 5, scale = 2)
    private BigDecimal igvPct = new BigDecimal("18.00");

    @Column(name = "tarifa_hora_defecto", nullable = false, precision = 8, scale = 2)
    private BigDecimal tarifaHoraDefecto = new BigDecimal("65.00");

    @Column(name = "formato_fecha", nullable = false, length = 20)
    private String formatoFecha = "d/m/Y";

    @Column(name = "color_marca", length = 20)
    private String colorMarca = "terracota";

    @Column(name = "pie_documentos", columnDefinition = "TEXT")
    private String pieDocumentos = "Gracias por confiar en TechDesk Pro Cloud.";

    @Column(name = "cuentas_bancarias_json", columnDefinition = "TEXT")
    private String cuentasBancariasJson;

    @Column(name = "detraccion_json", columnDefinition = "TEXT")
    private String detraccionJson;

    @Transient
    private Object cuentasBancarias;

    @Transient
    private Object detraccion;

    @Transient
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Lob
    @Column(name = "logo_base64", columnDefinition = "LONGTEXT")
    private String logoBase64;

    @Column(name = "smtp_host", length = 150)
    private String smtpHost = "smtp.nexusdesk.pe";

    @Column(name = "smtp_port", length = 10)
    private String smtpPort = "587";

    @Column(name = "smtp_seguridad", length = 20)
    private String smtpSeguridad = "tls";

    @Column(name = "smtp_user", length = 150)
    private String smtpUser = "notificaciones@nexusdesk.pe";

    @Column(name = "smtp_pass", length = 255)
    private String smtpPass = "";

    @Column(name = "smtp_from", length = 150)
    private String smtpFrom = "notificaciones@nexusdesk.pe";

    @Column(name = "smtp_nombre", length = 150)
    private String smtpNombre = "NexusDesk Soporte Cloud";

    @Column(name = "notif_copia", length = 150)
    private String notifCopia = "mesa@nexusdesk.pe";

    @Column(name = "notif_ticket_creado")
    private Boolean notifTicketCreado = true;

    @Column(name = "notif_ticket_estado")
    private Boolean notifTicketEstado = true;

    @Column(name = "notif_ticket_respuesta")
    private Boolean notifTicketRespuesta = true;

    public CompanyConfig() {}

    public Short getId() { return id; }
    public void setId(Short id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWeb() { return web; }
    public void setWeb(String web) { this.web = web; }

    public String getMonedaSimbolo() { return monedaSimbolo; }
    public void setMonedaSimbolo(String monedaSimbolo) { this.monedaSimbolo = monedaSimbolo; }

    public String getMonedaCodigo() { return monedaCodigo; }
    public void setMonedaCodigo(String monedaCodigo) { this.monedaCodigo = monedaCodigo; }

    public String getMonedaPos() { return monedaPos; }
    public void setMonedaPos(String monedaPos) { this.monedaPos = monedaPos; }

    public Boolean getMonedaEspacio() { return monedaEspacio; }
    public void setMonedaEspacio(Boolean monedaEspacio) { this.monedaEspacio = monedaEspacio; }

    public Integer getMonedaDecimales() { return monedaDecimales; }
    public void setMonedaDecimales(Integer monedaDecimales) { this.monedaDecimales = monedaDecimales; }

    public String getSepDecimal() { return sepDecimal; }
    public void setSepDecimal(String sepDecimal) { this.sepDecimal = sepDecimal; }

    public String getSepMiles() { return sepMiles; }
    public void setSepMiles(String sepMiles) { this.sepMiles = sepMiles; }

    public BigDecimal getIgvPct() { return igvPct; }
    public void setIgvPct(BigDecimal igvPct) { this.igvPct = igvPct; }

    public BigDecimal getTarifaHoraDefecto() { return tarifaHoraDefecto; }
    public void setTarifaHoraDefecto(BigDecimal tarifaHoraDefecto) { this.tarifaHoraDefecto = tarifaHoraDefecto; }

    public String getFormatoFecha() { return formatoFecha; }
    public void setFormatoFecha(String formatoFecha) { this.formatoFecha = formatoFecha; }

    public String getLogoBase64() { return logoBase64; }
    public void setLogoBase64(String logoBase64) { this.logoBase64 = logoBase64; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getEslogan() { return eslogan; }
    public void setEslogan(String eslogan) { this.eslogan = eslogan; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getHorarioAtencion() { return horarioAtencion; }
    public void setHorarioAtencion(String horarioAtencion) { this.horarioAtencion = horarioAtencion; }

    public String getSmtpHost() { return smtpHost; }
    public void setSmtpHost(String smtpHost) { this.smtpHost = smtpHost; }

    public String getSmtpPort() { return smtpPort; }
    public void setSmtpPort(String smtpPort) { this.smtpPort = smtpPort; }

    public String getSmtpSeguridad() { return smtpSeguridad; }
    public void setSmtpSeguridad(String smtpSeguridad) { this.smtpSeguridad = smtpSeguridad; }

    public String getSmtpUser() { return smtpUser; }
    public void setSmtpUser(String smtpUser) { this.smtpUser = smtpUser; }

    public String getSmtpPass() { return smtpPass; }
    public void setSmtpPass(String smtpPass) { this.smtpPass = smtpPass; }

    public String getSmtpFrom() { return smtpFrom; }
    public void setSmtpFrom(String smtpFrom) { this.smtpFrom = smtpFrom; }

    public String getSmtpNombre() { return smtpNombre; }
    public void setSmtpNombre(String smtpNombre) { this.smtpNombre = smtpNombre; }

    public String getNotifCopia() { return notifCopia; }
    public void setNotifCopia(String notifCopia) { this.notifCopia = notifCopia; }

    public Boolean getNotifTicketCreado() { return notifTicketCreado; }
    public void setNotifTicketCreado(Boolean notifTicketCreado) { this.notifTicketCreado = notifTicketCreado; }

    public Boolean getNotifTicketEstado() { return notifTicketEstado; }
    public void setNotifTicketEstado(Boolean notifTicketEstado) { this.notifTicketEstado = notifTicketEstado; }

    public Boolean getNotifTicketRespuesta() { return notifTicketRespuesta; }
    public void setNotifTicketRespuesta(Boolean notifTicketRespuesta) { this.notifTicketRespuesta = notifTicketRespuesta; }

    public String getDocEtiqueta() { return docEtiqueta; }
    public void setDocEtiqueta(String docEtiqueta) { this.docEtiqueta = docEtiqueta; }

    public String getGiro() { return giro; }
    public void setGiro(String giro) { this.giro = giro; }

    public String getZonaHoraria() { return zonaHoraria; }
    public void setZonaHoraria(String zonaHoraria) { this.zonaHoraria = zonaHoraria; }

    public String getImpuestoNombre() { return impuestoNombre; }
    public void setImpuestoNombre(String impuestoNombre) { this.impuestoNombre = impuestoNombre; }

    public String getColorMarca() { return colorMarca; }
    public void setColorMarca(String colorMarca) { this.colorMarca = colorMarca; }

    public String getPieDocumentos() { return pieDocumentos; }
    public void setPieDocumentos(String pieDocumentos) { this.pieDocumentos = pieDocumentos; }

    @JsonIgnore
    public String getCuentasBancariasJson() { return cuentasBancariasJson; }
    public void setCuentasBancariasJson(String cuentasBancariasJson) { this.cuentasBancariasJson = cuentasBancariasJson; }

    @JsonIgnore
    public String getDetraccionJson() { return detraccionJson; }
    public void setDetraccionJson(String detraccionJson) { this.detraccionJson = detraccionJson; }

    @Transient
    @JsonProperty("cuentasBancarias")
    public Object getCuentasBancarias() {
        if (this.cuentasBancarias != null) {
            return this.cuentasBancarias;
        }
        if (this.cuentasBancariasJson != null && !this.cuentasBancariasJson.isBlank()) {
            try {
                this.cuentasBancarias = OBJECT_MAPPER.readValue(this.cuentasBancariasJson, Object.class);
                return this.cuentasBancarias;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @JsonProperty("cuentasBancarias")
    public void setCuentasBancarias(Object node) {
        this.cuentasBancarias = node;
        if (node != null) {
            try {
                this.cuentasBancariasJson = OBJECT_MAPPER.writeValueAsString(node);
            } catch (Exception e) {
                this.cuentasBancariasJson = null;
            }
        } else {
            this.cuentasBancariasJson = null;
        }
    }

    @Transient
    @JsonProperty("detraccion")
    public Object getDetraccion() {
        if (this.detraccion != null) {
            return this.detraccion;
        }
        if (this.detraccionJson != null && !this.detraccionJson.isBlank()) {
            try {
                this.detraccion = OBJECT_MAPPER.readValue(this.detraccionJson, Object.class);
                return this.detraccion;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @JsonProperty("detraccion")
    public void setDetraccion(Object node) {
        this.detraccion = node;
        if (node != null) {
            try {
                this.detraccionJson = OBJECT_MAPPER.writeValueAsString(node);
            } catch (Exception e) {
                this.detraccionJson = null;
            }
        } else {
            this.detraccionJson = null;
        }
    }

    @PostLoad
    public void postLoad() {
        getCuentasBancarias();
        getDetraccion();
    }

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        if (this.cuentasBancarias != null) {
            try {
                this.cuentasBancariasJson = OBJECT_MAPPER.writeValueAsString(this.cuentasBancarias);
            } catch (Exception e) {
                // keep current
            }
        }
        if (this.detraccion != null) {
            try {
                this.detraccionJson = OBJECT_MAPPER.writeValueAsString(this.detraccion);
            } catch (Exception e) {
                // keep current
            }
        }
    }
}
