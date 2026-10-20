package com.nexusdesk.auth.repository;

import com.nexusdesk.auth.model.BackupRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BackupRepositoryTest {

    @Autowired
    private BackupRepository backupRepository;

    @Test
    @DisplayName("Test 1: Guardar y recuperar un registro de respaldo en BD real")
    void testGuardarYRecuperarRespaldo() {
        BackupRecord record = new BackupRecord();
        record.setNombre("nexusdesk_backup_20261023_010000_manual.sql.gz");
        record.setTipo("manual");
        record.setFecha(LocalDateTime.now());
        record.setTamanoLegible("1.50 MB");
        record.setTamanoBytes(1572864L);
        record.setTablasRespaldadas(32);
        record.setFilasRespaldadas(1840);
        record.setChecksumSha256("E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855");
        record.setEstado("COMPLETADO");
        record.setCreadoPor("Administrador");

        BackupRecord saved = backupRepository.save(record);
        assertNotNull(saved.getId(), "El ID debe ser autogenerado");

        Optional<BackupRecord> retrieved = backupRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent(), "El registro debe existir en BD");
        assertEquals("nexusdesk_backup_20261023_010000_manual.sql.gz", retrieved.get().getNombre());
        assertEquals("manual", retrieved.get().getTipo());
        assertEquals(32, retrieved.get().getTablasRespaldadas());
        assertEquals("COMPLETADO", retrieved.get().getEstado());
    }

    @Test
    @DisplayName("Test 2: Listar respaldos ordenados cronológicamente descendente")
    void testListarRespaldosOrdenadosPorFechaDesc() {
        BackupRecord r1 = new BackupRecord();
        r1.setNombre("backup_ayer.sql.gz");
        r1.setTipo("auto");
        r1.setFecha(LocalDateTime.now().minusDays(1));
        r1.setTamanoLegible("1.20 MB");
        r1.setTamanoBytes(1258291L);
        r1.setTablasRespaldadas(30);
        r1.setFilasRespaldadas(1500);
        r1.setChecksumSha256("A1B2C3D4E5F60718");
        backupRepository.save(r1);

        BackupRecord r2 = new BackupRecord();
        r2.setNombre("backup_hoy.sql.gz");
        r2.setTipo("manual");
        r2.setFecha(LocalDateTime.now());
        r2.setTamanoLegible("1.45 MB");
        r2.setTamanoBytes(1520435L);
        r2.setTablasRespaldadas(32);
        r2.setFilasRespaldadas(1840);
        r2.setChecksumSha256("F6E5D4C3B2A10987");
        backupRepository.save(r2);

        List<BackupRecord> list = backupRepository.findAllByOrderByFechaDesc();
        assertFalse(list.isEmpty());
        assertTrue(list.size() >= 2);

        // El primer elemento debe ser el más reciente (backup_hoy)
        assertEquals("backup_hoy.sql.gz", list.get(0).getNombre());
    }

    @Test
    @DisplayName("Test 3: Eliminar registro de respaldo existente")
    void testEliminarRespaldo() {
        BackupRecord record = new BackupRecord();
        record.setNombre("backup_para_borrar.sql.gz");
        record.setTipo("manual");
        record.setTamanoLegible("500 KB");
        record.setTamanoBytes(512000L);
        record.setTablasRespaldadas(10);
        record.setFilasRespaldadas(200);
        record.setChecksumSha256("DEL1234567890ABC");

        BackupRecord saved = backupRepository.save(record);
        Long id = saved.getId();
        assertNotNull(id);

        backupRepository.deleteById(id);
        Optional<BackupRecord> deleted = backupRepository.findById(id);
        assertTrue(deleted.isEmpty(), "El registro debe haber sido eliminado");
    }
}
