package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fogo-na-panela-ws/empresas")
@RequiredArgsConstructor
public class EmpresaController {


    private final EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<Empresa> cadastrarEmpresa(@Valid @RequestBody Empresa empresa) {
        Empresa novaEmpresa = empresaService.salvar(empresa);
        return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED);
    }
}