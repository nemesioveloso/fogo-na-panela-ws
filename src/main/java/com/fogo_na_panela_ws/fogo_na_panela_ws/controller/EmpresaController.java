package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/fogo-na-panela-ws/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<ApiResponse> cadastrarEmpresa(@Valid @RequestBody Empresa empresa) {
        try {
            empresaService.salvar(empresa);
            ApiResponse response = new ApiResponse("Sucesso", "Usuário cadastrado com sucesso.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            ApiResponse response = new ApiResponse("Erro", e.getReason());
            return new ResponseEntity<>(response, e.getStatusCode());
        }
    }
}
