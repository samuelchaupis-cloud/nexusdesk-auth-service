package com.nexusdesk.auth.service;

import com.nexusdesk.auth.dto.CreateUserRequest;
import com.nexusdesk.auth.dto.UpdateUserRequest;
import com.nexusdesk.auth.dto.UserDto;
import com.nexusdesk.auth.dto.UserStatsDto;
import com.nexusdesk.auth.entity.Rol;
import com.nexusdesk.auth.entity.Usuario;
import com.nexusdesk.auth.repository.RolRepository;
import com.nexusdesk.auth.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final AuditService auditService;

    public UserService(UsuarioRepository usuarioRepository,
                       RolRepository rolRepository,
                       PasswordEncoder passwordEncoder,
                       AuthService authService,
                       AuditService auditService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<UserDto> listUsers(String query, Short rolId, Pageable pageable) {
        return usuarioRepository.searchUsers(query, rolId, pageable)
                .map(authService::mapToDto);
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        return authService.mapToDto(usuario);
    }

    @Transactional
    public UserDto createUser(CreateUserRequest request, Long currentUserId, String currentUserName) {
        if (usuarioRepository.existsByUsuario(request.getUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no válido con ID: " + request.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombres(request.getNombres().trim());
        usuario.setApellidos(request.getApellidos().trim());
        usuario.setUsuario(request.getUsuario().trim().toLowerCase());
        usuario.setEmail(request.getEmail().trim().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        usuario.setTelefono(request.getTelefono());
        usuario.setCargo(request.getCargo());
        if (request.getAvatarColor() != null && !request.getAvatarColor().isBlank()) {
            usuario.setAvatarColor(request.getAvatarColor());
        }
        usuario.setEstado(1);
        usuario.setClienteId(request.getClienteId());

        Usuario saved = usuarioRepository.save(usuario);

        auditService.logEvent(
                currentUserId,
                currentUserName,
                "Creó usuario del sistema",
                "@" + saved.getUsuario() + " (" + saved.getNombreCompleto() + ") - Rol: " + rol.getNombre(),
                "bi-person-plus",
                "#10b981"
        );

        return authService.mapToDto(saved);
    }

    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequest request, Long currentUserId, String currentUserName) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        // INVARIANTE DE SEGURIDAD: Un administrador no puede desactivar su propia cuenta activa
        if (currentUserId != null && currentUserId.equals(id)) {
            if (request.getEstado() != null && request.getEstado() == 0) {
                throw new IllegalArgumentException("No puedes desactivar tu propia cuenta activa de administrador.");
            }
        }

        if (!usuario.getEmail().equalsIgnoreCase(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso");
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no válido con ID: " + request.getRolId()));

        usuario.setRol(rol);
        usuario.setNombres(request.getNombres().trim());
        usuario.setApellidos(request.getApellidos().trim());
        usuario.setEmail(request.getEmail().trim().toLowerCase());
        usuario.setTelefono(request.getTelefono());
        usuario.setCargo(request.getCargo());
        if (request.getAvatarColor() != null && !request.getAvatarColor().isBlank()) {
            usuario.setAvatarColor(request.getAvatarColor());
        }
        if (request.getEstado() != null) {
            usuario.setEstado(request.getEstado());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getPassword().trim().length() < 6) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
            }
            usuario.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        }
        if (request.getClienteId() != null) {
            usuario.setClienteId(request.getClienteId());
        }

        Usuario updated = usuarioRepository.save(usuario);

        auditService.logEvent(
                currentUserId,
                currentUserName,
                "Modificó usuario del sistema",
                "@" + updated.getUsuario() + " (" + updated.getNombreCompleto() + ") - Estado: " + (updated.getEstado() == 1 ? "Activo" : "Bloqueado"),
                "bi-person-gear",
                "#0ea5e9"
        );

        return authService.mapToDto(updated);
    }

    @Transactional
    public void deleteUser(Long id, Long currentUserId, String currentUserName) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        // INVARIANTE DE SEGURIDAD: Un administrador no puede eliminar su propia cuenta activa
        if (currentUserId != null && currentUserId.equals(id)) {
            throw new IllegalArgumentException("No puedes eliminar tu propia cuenta activa de administrador.");
        }
        if (id == 1L) {
            throw new IllegalArgumentException("No se puede eliminar la cuenta del administrador principal del sistema.");
        }

        // Soft delete: estado = 0
        usuario.setEstado(0);
        usuarioRepository.save(usuario);

        auditService.logEvent(
                currentUserId,
                currentUserName,
                "Desactivó cuenta de usuario",
                "@" + usuario.getUsuario() + " (" + usuario.getNombreCompleto() + ")",
                "bi-person-x",
                "#ef4444"
        );
    }

    @Transactional(readOnly = true)
    public List<UserDto> listActiveTechnicians() {
        return usuarioRepository.findTechniciansActive().stream()
                .map(authService::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Rol> listRoles() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserStatsDto getUserStats() {
        long total = usuarioRepository.count();
        long activos = usuarioRepository.countByEstado(1);
        long tecnicos = usuarioRepository.countByRolNombre("Técnico");
        long bloqueados = usuarioRepository.countByEstado(0);
        return new UserStatsDto(total, activos, tecnicos, bloqueados);
    }
}
