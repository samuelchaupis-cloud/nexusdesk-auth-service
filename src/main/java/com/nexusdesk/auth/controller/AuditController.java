package com.nexusdesk.auth.controller;

import com.nexusdesk.auth.dto.AuditEventDto;
import com.nexusdesk.auth.dto.AuditStatsDto;
import com.nexusdesk.auth.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<Page<AuditEventDto>> listAuditEvents(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AuditEventDto> events = auditService.listEvents(q, usuarioId, desde, hasta, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/stats")
    public ResponseEntity<AuditStatsDto> getAuditStats() {
        AuditStatsDto stats = auditService.getStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {

        String csv = auditService.exportCsv(q, usuarioId, desde, hasta);
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

        String filename = "auditoria_" + LocalDate.now() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(bytes);
    }
}
