package com.example.base.controller;

import com.example.base.dto.UserCreateDTO;
import com.example.base.dto.UserPatchDTO;
import com.example.base.dto.UserResponseDTO;
import com.example.base.exception.UnauthorizedException;
import com.example.base.model.User;
import com.example.base.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UserResponseDTO> patchUpdate(
            @PathVariable Long id,
            @RequestBody @Valid UserPatchDTO dto,
            @AuthenticationPrincipal User principal) {

        User updated = userService.partialUpdate(id, dto, principal.getId(), principal.getRoles());
        return ResponseEntity.ok(UserResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Usuário inativado com sucesso"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserCreateDTO dto) {
        User user = userService.create(dto);
        log.info("Usuário cadastrado ID: {}", user.getId());
        return ResponseEntity.status(201)
                .body(Map.of("message", "Usuário cadastrado com sucesso"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal User principal) {
        if (principal == null) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }
        return ResponseEntity.ok(UserResponseDTO.from(principal));
    }
}
