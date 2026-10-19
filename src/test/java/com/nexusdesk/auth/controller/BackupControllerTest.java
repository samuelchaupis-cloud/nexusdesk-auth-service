package com.nexusdesk.auth.controller;

import com.nexusdesk.auth.model.BackupRecord;
import com.nexusdesk.auth.repository.BackupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BackupControllerTest {

    @Autowired
    private BackupRepository backupRepository;

    private BackupController backupController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        backupController = new BackupController(backupRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(backupController).build();
    }

    @Test
    @DisplayName("Admin: Listar respaldos exitosamente a través de MockMvc (HTTP 200)")
    void testListBackupsViaMockMvc() throws Exception {
        BackupRecord record = new BackupRecord();
        record.setNombre("test_backup.sql.gz");
        record.setTipo("manual");
        record.setFecha(LocalDateTime.now());
        record.setTamanoLegible("1.0 MB");
        record.setTamanoBytes(1048576L);
        record.setTablasRespaldadas(25);
        record.setFilasRespaldadas(1200);
        record.setChecksumSha256("TESTCHECKSUM1234");
        record.setEstado("COMPLETADO");
        record.setCreadoPor("Admin");
        backupRepository.save(record);

        mockMvc.perform(get("/api/auth/backups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].nombre", notNullValue()))
                .andExpect(jsonPath("$[0].checksumSha256", notNullValue()));
    }

    @Test
    @DisplayName("Admin: Crear nuevo respaldo exitosamente a través de MockMvc (HTTP 200)")
    void testCreateBackupViaMockMvc() throws Exception {
        String payload = "{\"tipo\":\"manual\"}";

        mockMvc.perform(post("/api/auth/backups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", containsString("nexusdesk_backup_")))
                .andExpect(jsonPath("$.tipo", is("manual")))
                .andExpect(jsonPath("$.estado", is("COMPLETADO")))
                .andExpect(jsonPath("$.tablasRespaldadas", is(32)));
    }

    @Test
    @DisplayName("Admin: Eliminar un respaldo existente a través de MockMvc (HTTP 204)")
    void testDeleteBackupViaMockMvc() throws Exception {
        BackupRecord record = new BackupRecord();
        record.setNombre("to_delete.sql.gz");
        record.setTipo("manual");
        record.setTamanoLegible("100 KB");
        record.setTamanoBytes(102400L);
        record.setTablasRespaldadas(5);
        record.setFilasRespaldadas(50);
        record.setChecksumSha256("DELTEST987654321");
        BackupRecord saved = backupRepository.save(record);

        mockMvc.perform(delete("/api/auth/backups/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(backupRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("Direct Controller Invocation: Creación con tipo por defecto y verificación de campos")
    void testDirectControllerInvocation() {
        // Invocación con request null
        ResponseEntity<BackupRecord> resNull = backupController.createBackup(null);
        assertEquals(HttpStatus.OK, resNull.getStatusCode());
        assertNotNull(resNull.getBody());
        assertEquals("manual", resNull.getBody().getTipo());
        assertTrue(resNull.getBody().getNombre().startsWith("nexusdesk_backup_"));

        // Invocación con tipo explícito
        BackupController.CreateBackupRequest req = new BackupController.CreateBackupRequest("auto");
        ResponseEntity<BackupRecord> resAuto = backupController.createBackup(req);
        assertEquals(HttpStatus.OK, resAuto.getStatusCode());
        assertNotNull(resAuto.getBody());
        assertEquals("auto", resAuto.getBody().getTipo());

        // Listar
        ResponseEntity<List<BackupRecord>> listRes = backupController.listBackups();
        assertEquals(HttpStatus.OK, listRes.getStatusCode());
        assertNotNull(listRes.getBody());
        assertTrue(listRes.getBody().size() >= 2);

        // Eliminar
        ResponseEntity<Void> delRes = backupController.deleteBackup(resAuto.getBody().getId());
        assertEquals(HttpStatus.NO_CONTENT, delRes.getStatusCode());
    }
}
