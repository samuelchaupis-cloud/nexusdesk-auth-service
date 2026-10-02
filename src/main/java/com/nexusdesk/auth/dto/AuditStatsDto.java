package com.nexusdesk.auth.dto;

public class AuditStatsDto {

    private long total;
    private long hoy;
    private long accesos;
    private long activos;

    public AuditStatsDto() {}

    public AuditStatsDto(long total, long hoy, long accesos, long activos) {
        this.total = total;
        this.hoy = hoy;
        this.accesos = accesos;
        this.activos = activos;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public long getHoy() { return hoy; }
    public void setHoy(long hoy) { this.hoy = hoy; }

    public long getAccesos() { return accesos; }
    public void setAccesos(long accesos) { this.accesos = accesos; }

    public long getActivos() { return activos; }
    public void setActivos(long activos) { this.activos = activos; }
}
