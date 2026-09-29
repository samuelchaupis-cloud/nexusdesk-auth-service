package com.nexusdesk.auth.controller;

import com.nexusdesk.auth.dto.ChangePasswordRequest;
import com.nexusdesk.auth.dto.LoginRequest;
import com.nexusdesk.auth.dto.LoginResponse;
import com.nexusdesk.auth.dto.UserDto;
import com.nexusdesk.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getProfile(@RequestHeader("X-User-Id") Long userId) {
        UserDto profile = authService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestHeader("X-User-Id") Long userId,
                                               @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }
}
