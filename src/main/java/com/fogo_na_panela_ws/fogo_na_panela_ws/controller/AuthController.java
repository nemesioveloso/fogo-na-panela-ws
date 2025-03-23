package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.config.JwtUtil;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.LoginResponseDTO;
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
    public ResponseEntity<?> autenticarEmpresa(@RequestBody Empresa credenciais) {
        Optional<Empresa> empresaOpt = empresaRepository.findByEmail(credenciais.getEmail());

        if (empresaOpt.isPresent() && passwordEncoder.matches(credenciais.getSenha(), empresaOpt.get().getSenha())) {
            Empresa empresa = empresaOpt.get();
            String token = jwtUtil.gerarToken(empresa.getId(), null, "ADMIN");

            LoginResponseDTO response = new LoginResponseDTO("Sucesso", "Seja Bem Vindo", token, "ADMIN");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(credenciais.getEmail());

        if (usuarioOpt.isPresent() && passwordEncoder.matches(credenciais.getSenha(), usuarioOpt.get().getSenha())) {
            Usuario usuario = usuarioOpt.get();
            String permissao = usuario.getPermissao().name();

            String token = jwtUtil.gerarToken(usuario.getEmpresa().getId(), usuario.getId(), permissao);

            LoginResponseDTO response = new LoginResponseDTO("Sucesso", "Seja Bem Vindo", token, permissao);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse response = new ApiResponse("Erro", "Credenciais inválidas.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }



}
