package com.example.base.controller;

import com.example.base.dto.UserCreateDTO;
import com.example.base.enums.Role;
import com.example.base.model.User;
import com.example.base.security.JWTService;
import com.example.base.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,
            @RequestHeader("Authorization") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        Long requesterId = jwtService.extractUserId(token);
        Set<Role> requesterRoles = jwtService.extractRoles(token);

        User updated = userService.partialUpdate(id, updates, requesterId, requesterRoles);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Set<Role> roles = jwtService.extractRoles(token);

        if (!roles.contains(Role.ADMIN)) {
            return ResponseEntity.status(403).body(Map.of(
                    "message", "Apenas administradores podem inativar usuários."
            ));
        }

        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Usuário inativado com sucesso"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserCreateDTO dto) {
        userService.create(dto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário cadastrado com sucesso");

        return ResponseEntity.status(201).body(response);
    }

}
