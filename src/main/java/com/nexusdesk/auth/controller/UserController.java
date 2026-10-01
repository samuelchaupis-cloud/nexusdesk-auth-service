package com.nexusdesk.auth.controller;

import com.nexusdesk.auth.dto.CreateUserRequest;
import com.nexusdesk.auth.dto.UpdateUserRequest;
import com.nexusdesk.auth.dto.UserDto;
import com.nexusdesk.auth.dto.UserStatsDto;
import com.nexusdesk.auth.entity.Rol;
import com.nexusdesk.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> listUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Short rolId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDto> users = userService.listUsers(q, rolId, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/stats")
    public ResponseEntity<UserStatsDto> getUserStats() {
        UserStatsDto stats = userService.getUserStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = "X-User-Name", required = false) String userNameHeader,
            @Valid @RequestBody CreateUserRequest request) {
        if (role != null && !"Administrador".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Acceso denegado: se requiere rol Administrador"));
        }

        Long currentUserId = parseLongSafe(userIdHeader);
        String currentUserName = (userNameHeader != null && !userNameHeader.isBlank()) ? userNameHeader : "Administrador";

        try {
            UserDto created = userService.createUser(request, currentUserId, currentUserName);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = "X-User-Name", required = false) String userNameHeader,
            @Valid @RequestBody UpdateUserRequest request) {
        if (role != null && !"Administrador".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Acceso denegado: se requiere rol Administrador"));
        }

        Long currentUserId = parseLongSafe(userIdHeader);
        String currentUserName = (userNameHeader != null && !userNameHeader.isBlank()) ? userNameHeader : "Administrador";

        try {
            UserDto updated = userService.updateUser(id, request, currentUserId, currentUserName);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = "X-User-Name", required = false) String userNameHeader) {
        if (role != null && !"Administrador".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Acceso denegado: se requiere rol Administrador"));
        }

        Long currentUserId = parseLongSafe(userIdHeader);
        String currentUserName = (userNameHeader != null && !userNameHeader.isBlank()) ? userNameHeader : "Administrador";

        try {
            userService.deleteUser(id, currentUserId, currentUserName);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/technicians")
    public ResponseEntity<List<UserDto>> listTechnicians() {
        List<UserDto> techs = userService.listActiveTechnicians();
        return ResponseEntity.ok(techs);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> listRoles() {
        List<Rol> roles = userService.listRoles();
        return ResponseEntity.ok(roles);
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
