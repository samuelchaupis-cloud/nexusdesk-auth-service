package com.nexusdesk.auth.service;

import com.nexusdesk.auth.dto.AuditEventDto;
import com.nexusdesk.auth.dto.AuditStatsDto;
import com.nexusdesk.auth.entity.Actividad;
import com.nexusdesk.auth.repository.ActividadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AuditService {

    private final ActividadRepository actividadRepository;

    public AuditService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    @Transactional
    public void logEvent(Long usuarioId, String usuarioNombre, String accion, String detalle, String icono, String color) {
        try {
            Actividad actividad = new Actividad(usuarioId, usuarioNombre, accion, detalle, icono, color);
            actividadRepository.save(actividad);
        } catch (Exception e) {
            // No interrumpir la transacción principal si la auditoría falla
        }
    }

    @Transactional(readOnly = true)
    public Page<AuditEventDto> listEvents(String q, Long usuarioId, String desdeStr, String hastaStr, Pageable pageable) {
        LocalDateTime desde = null;
        LocalDateTime hasta = null;

        if (desdeStr != null && !desdeStr.isBlank()) {
            try {
                desde = LocalDate.parse(desdeStr.trim()).atStartOfDay();
            } catch (Exception ignored) {}
        }
        if (hastaStr != null && !hastaStr.isBlank()) {
            try {
                hasta = LocalDate.parse(hastaStr.trim()).atTime(LocalTime.MAX);
            } catch (Exception ignored) {}
        }

        String search = (q != null && !q.isBlank()) ? q.trim() : null;

        return actividadRepository.searchActivities(search, usuarioId, desde, hasta, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public AuditStatsDto getStats() {
        long total = actividadRepository.count();
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        long hoy = actividadRepository.countSince(startOfToday);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        long accesos = actividadRepository.countLoginSince(sevenDaysAgo);
        long activos = actividadRepository.countDistinctActiveUsersSince(sevenDaysAgo);

        return new AuditStatsDto(total, hoy, accesos, activos);
    }

    @Transactional(readOnly = true)
    public String exportCsv(String q, Long usuarioId, String desdeStr, String hastaStr) {
        LocalDateTime desde = null;
        LocalDateTime hasta = null;
        if (desdeStr != null && !desdeStr.isBlank()) {
            try { desde = LocalDate.parse(desdeStr.trim()).atStartOfDay(); } catch (Exception ignored) {}
        }
        if (hastaStr != null && !hastaStr.isBlank()) {
            try { hasta = LocalDate.parse(hastaStr.trim()).atTime(LocalTime.MAX); } catch (Exception ignored) {}
        }

        String search = (q != null && !q.isBlank()) ? q.trim() : null;
        List<Actividad> list = actividadRepository.searchActivitiesList(search, usuarioId, desde, hasta);

        StringBuilder sb = new StringBuilder();
        // UTF-8 BOM
        sb.append('\uFEFF');
        sb.append("Fecha;Usuario;Acción;Detalle\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Actividad a : list) {
            String f = a.getCreatedAt() != null ? a.getCreatedAt().format(dtf) : "—";
            String u = a.getUsuarioNombre() != null ? a.getUsuarioNombre() : "Sistema";
            String ac = a.getAccion() != null ? a.getAccion().replace(";", ",") : "";
            String det = a.getDetalle() != null ? a.getDetalle().replace(";", ",") : "";
            sb.append(f).append(";").append(u).append(";").append(ac).append(";").append(det).append("\n");
        }

        return sb.toString();
    }

    private AuditEventDto mapToDto(Actividad a) {
        return new AuditEventDto(
                a.getId(),
                a.getUsuarioId(),
                a.getUsuarioNombre(),
                a.getAccion(),
                a.getDetalle(),
                a.getIcono(),
                a.getColor(),
                a.getCreatedAt()
        );
    }
}
