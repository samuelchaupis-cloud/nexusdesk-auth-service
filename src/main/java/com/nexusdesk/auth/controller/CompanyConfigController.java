package com.nexusdesk.auth.controller;

import com.nexusdesk.auth.entity.CompanyConfig;
import com.nexusdesk.auth.repository.CompanyConfigRepository;
import com.nexusdesk.auth.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/company-config")
public class CompanyConfigController {

    private final CompanyConfigRepository repository;
    private final AuditService auditService;

    public CompanyConfigController(CompanyConfigRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<CompanyConfig> getConfig() {
        CompanyConfig config = repository.findById((short) 1)
                .orElseGet(() -> repository.save(new CompanyConfig()));
        return ResponseEntity.ok(config);
    }

    @PutMapping
    public ResponseEntity<CompanyConfig> updateConfig(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = "X-User-Name", required = false) String userNameHeader,
            @RequestBody CompanyConfig req) {
        req.setId((short) 1);
        CompanyConfig saved = repository.save(req);

        Long userId = parseLongSafe(userIdHeader);
        String userName = (userNameHeader != null && !userNameHeader.isBlank()) ? userNameHeader : "Administrador";

        auditService.logEvent(
                userId,
                userName,
                "Actualizó parámetros de la empresa",
                "Razón Social: " + saved.getRazonSocial() + ", RUC: " + saved.getRuc() + ", Moneda: " + saved.getMonedaSimbolo() + " (" + saved.getMonedaCodigo() + ")",
                "bi-building-gear",
                "#0ea5e9"
        );

        return ResponseEntity.ok(saved);
    }

    private Long parseLongSafe(String str) {
        if (str == null || str.isBlank()) return null;
        try {
            return Long.parseLong(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
