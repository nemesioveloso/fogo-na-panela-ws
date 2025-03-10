package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Usuario;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/fogo-na-panela-ws/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ApiResponse> cadastrarUsuario(@Valid @RequestBody Usuario usuario, HttpServletRequest request) {
        try {
            Long empresaId = (Long) request.getAttribute("empresaId");
            if (empresaId == null) {
                return new ResponseEntity<>(new ApiResponse("Erro", "Token inválido ou ausente."), HttpStatus.UNAUTHORIZED);
            }

            usuarioService.salvar(usuario, empresaId);
            ApiResponse response = new ApiResponse("Sucesso", "Usuário cadastrado com sucesso.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            ApiResponse response = new ApiResponse("Erro", e.getReason());
            return new ResponseEntity<>(response, e.getStatusCode());
        }
    }


}
