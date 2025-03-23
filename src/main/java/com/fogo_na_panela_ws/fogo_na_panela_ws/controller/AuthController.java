package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.config.JwtUtil;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Usuario;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> autenticarEmpresa(@RequestBody Empresa credenciais) {
        Optional<Empresa> empresaOpt = empresaRepository.findByEmail(credenciais.getEmail());

        if (empresaOpt.isPresent() && passwordEncoder.matches(credenciais.getSenha(), empresaOpt.get().getSenha())) {
            Empresa empresa = empresaOpt.get();

            // Se a permissão do "dono" for ADMIN, o token carrega o ID da empresa
            String token = jwtUtil.gerarToken(empresa.getId(), null);
            ApiResponse response = new ApiResponse("Sucesso", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Caso não seja uma empresa, verifica se é um usuário (funcionário)
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(credenciais.getEmail());

        if (usuarioOpt.isPresent() && passwordEncoder.matches(credenciais.getSenha(), usuarioOpt.get().getSenha())) {
            Usuario usuario = usuarioOpt.get();

            // Gera token com ID da empresa e ID do usuário
            String token = jwtUtil.gerarToken(usuario.getEmpresa().getId(), usuario.getId());
            ApiResponse response = new ApiResponse("Sucesso", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse response = new ApiResponse("Erro", "Credenciais inválidas.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
