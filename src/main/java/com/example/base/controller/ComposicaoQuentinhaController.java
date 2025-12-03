package com.example.base.controller;

import com.example.base.dto.ComposicaoQuentinhaCreateDTO;
import com.example.base.dto.ComposicaoQuentinhaResponseDTO;
import com.example.base.service.ComposicaoQuentinhaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-quentinha/{tipoId}/composicoes")
@RequiredArgsConstructor
public class ComposicaoQuentinhaController {

    private final ComposicaoQuentinhaService service;

    @GetMapping
    public ResponseEntity<List<ComposicaoQuentinhaResponseDTO>> listar(@PathVariable Long tipoId) {
        return ResponseEntity.ok(service.listarPorTipo(tipoId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ComposicaoQuentinhaResponseDTO> criar(
            @PathVariable Long tipoId,
            @Valid @RequestBody ComposicaoQuentinhaCreateDTO dto) {

        return ResponseEntity.status(201).body(service.criar(tipoId, dto));
    }

    @PatchMapping("/{composicaoId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ComposicaoQuentinhaResponseDTO> atualizar(
            @PathVariable Long tipoId,
            @PathVariable Long composicaoId,
            @Valid @RequestBody ComposicaoQuentinhaCreateDTO dto) {

        return ResponseEntity.ok(service.atualizar(tipoId, composicaoId, dto));
    }

    @DeleteMapping("/{composicaoId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Void> remover(
            @PathVariable Long tipoId,
            @PathVariable Long composicaoId) {

        service.remover(tipoId, composicaoId);
        return ResponseEntity.noContent().build();
    }
}
