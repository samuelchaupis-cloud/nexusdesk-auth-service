package com.nexusdesk.auth.repository;

import com.nexusdesk.auth.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Short> {
    Optional<Rol> findByNombre(String nombre);
}
