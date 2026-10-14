package com.nexusdesk.auth.repository;

import com.nexusdesk.auth.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE (u.usuario = :login OR u.email = :login) AND u.estado = 1")
    Optional<Usuario> findByUsuarioOrEmailActive(@Param("login") String login);

    Optional<Usuario> findByUsuario(String usuario);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);

    List<Usuario> findByRolIdAndEstado(Short rolId, Integer estado);

    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'Técnico' AND u.estado = 1")
    List<Usuario> findTechniciansActive();

    @Query("SELECT u FROM Usuario u WHERE (:query IS NULL OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.usuario) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))) AND (:rolId IS NULL OR u.rol.id = :rolId)")
    Page<Usuario> searchUsers(@Param("query") String query, @Param("rolId") Short rolId, Pageable pageable);

    long countByEstado(Integer estado);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.nombre = :rolNombre")
    long countByRolNombre(@Param("rolNombre") String rolNombre);
}
