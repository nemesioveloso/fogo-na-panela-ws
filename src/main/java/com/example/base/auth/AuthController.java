package com.example.base.auth;

import com.example.base.exception.UnauthorizedException;
import com.example.base.model.User;
import com.example.base.repository.UserRepository;
import com.example.base.security.JWTService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) throws Exception {
        AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmailAndActiveTrue(request.getUsernameOrEmail())
                .or(() -> userRepository.findByUsernameAndActiveTrue(request.getUsernameOrEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado ou inativo."));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDTO("Login realizado com sucesso", accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || !jwtService.isValid(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Refresh token inválido");
        }

        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUsernameAndActiveTrue(username)
                .or(() -> userRepository.findByEmailAndActiveTrue(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado ou inativo."));

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDTO("Token renovado com sucesso", newAccessToken, newRefreshToken));
    }
}

