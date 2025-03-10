package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.config.JwtUtil;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/fogo-na-panela-ws/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmpresaRepository empresaRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> autenticarEmpresa(@RequestBody Empresa credenciais) {
        Optional<Empresa> empresaOpt = empresaRepository.findByEmail(credenciais.getEmail());



        if (empresaOpt.isPresent() && passwordEncoder.matches(credenciais.getSenha(), empresaOpt.get().getSenha())) {
            String token = jwtUtil.gerarToken(empresaOpt.get().getId());
            ApiResponse response = new ApiResponse("Sucesso", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse response = new ApiResponse("Erro", "Credenciais inválidas.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
