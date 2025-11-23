package com.example.base.controller;

import com.example.base.dto.TipoQuentinhaDTO;
import com.example.base.dto.TipoQuentinhaUpdateDTO;
import com.example.base.service.TipoQuentinhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-quentinha")
@RequiredArgsConstructor
public class TipoQuentinhaController {

    private final TipoQuentinhaService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TipoQuentinhaDTO> criar(
            @RequestBody TipoQuentinhaUpdateDTO dto) {

        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TipoQuentinhaDTO> atualizarParcial(
            @PathVariable Long id,
            @RequestBody TipoQuentinhaUpdateDTO dto) {

        return ResponseEntity.ok(service.atualizarParcial(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {

        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoQuentinhaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<TipoQuentinhaDTO>> listarAtivos() {
        return ResponseEntity.ok(service.listarTodosAtivos());
    }

    @GetMapping
    public ResponseEntity<List<TipoQuentinhaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
