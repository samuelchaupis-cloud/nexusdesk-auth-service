package com.nexusdesk.auth.repository;

import com.nexusdesk.auth.entity.Actividad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    @Query("SELECT a FROM Actividad a WHERE " +
           "(:q IS NULL OR LOWER(a.accion) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(a.detalle) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
           "(:usuarioId IS NULL OR a.usuarioId = :usuarioId) AND " +
           "(:desde IS NULL OR a.createdAt >= :desde) AND " +
           "(:hasta IS NULL OR a.createdAt <= :hasta) " +
           "ORDER BY a.createdAt DESC, a.id DESC")
    Page<Actividad> searchActivities(@Param("q") String q,
                                    @Param("usuarioId") Long usuarioId,
                                    @Param("desde") LocalDateTime desde,
                                    @Param("hasta") LocalDateTime hasta,
                                    Pageable pageable);

    @Query("SELECT a FROM Actividad a WHERE " +
           "(:q IS NULL OR LOWER(a.accion) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(a.detalle) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
           "(:usuarioId IS NULL OR a.usuarioId = :usuarioId) AND " +
           "(:desde IS NULL OR a.createdAt >= :desde) AND " +
           "(:hasta IS NULL OR a.createdAt <= :hasta) " +
           "ORDER BY a.createdAt DESC, a.id DESC")
    List<Actividad> searchActivitiesList(@Param("q") String q,
                                        @Param("usuarioId") Long usuarioId,
                                        @Param("desde") LocalDateTime desde,
                                        @Param("hasta") LocalDateTime hasta);

    @Query("SELECT COUNT(a) FROM Actividad a WHERE a.createdAt >= :since")
    long countSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(a) FROM Actividad a WHERE LOWER(a.accion) LIKE '%sesi%' AND a.createdAt >= :since")
    long countLoginSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(DISTINCT a.usuarioId) FROM Actividad a WHERE a.usuarioId IS NOT NULL AND a.createdAt >= :since")
    long countDistinctActiveUsersSince(@Param("since") LocalDateTime since);
}
