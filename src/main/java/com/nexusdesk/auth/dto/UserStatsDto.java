package com.nexusdesk.auth.dto;

public class UserStatsDto {

    private long total;
    private long activos;
    private long tecnicos;
    private long bloqueados;

    public UserStatsDto() {}

    public UserStatsDto(long total, long activos, long tecnicos, long bloqueados) {
        this.total = total;
        this.activos = activos;
        this.tecnicos = tecnicos;
        this.bloqueados = bloqueados;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public long getActivos() { return activos; }
    public void setActivos(long activos) { this.activos = activos; }

    public long getTecnicos() { return tecnicos; }
    public void setTecnicos(long tecnicos) { this.tecnicos = tecnicos; }

    public long getBloqueados() { return bloqueados; }
    public void setBloqueados(long bloqueados) { this.bloqueados = bloqueados; }
}
