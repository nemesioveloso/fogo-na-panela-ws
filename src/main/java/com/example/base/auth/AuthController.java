package com.example.base.auth;

import com.example.base.model.User;
import com.example.base.repository.UserRepository;
import com.example.base.security.JWTService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDTO request) throws Exception {
        AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword());

        Authentication authentication = authManager.authenticate(authToken);

        User user = userRepository.findByEmail(request.getUsernameOrEmail())
                .or(() -> userRepository.findByUsername(request.getUsernameOrEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        String token = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login realizado com sucesso");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}

