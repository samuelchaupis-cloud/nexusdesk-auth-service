package com.nexusdesk.auth.repository;

import com.nexusdesk.auth.model.BackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupRepository extends JpaRepository<BackupRecord, Long> {
    List<BackupRecord> findAllByOrderByFechaDesc();
}
