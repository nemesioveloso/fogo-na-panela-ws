package com.example.base.controller;

import com.example.base.dto.ItemCreateDTO;
import com.example.base.dto.ItemResponseDTO;
import com.example.base.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/itens")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ItemResponseDTO> criar(@Valid @RequestBody ItemCreateDTO dto) {
        return ResponseEntity.status(201).body(itemService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarTodos() {
        return ResponseEntity.ok(itemService.listarTodosAtivos());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ItemResponseDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(itemService.listarPorCategoria(categoriaId));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ItemResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemCreateDTO dto
    ) {
        return ResponseEntity.ok(itemService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        itemService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Long id) {
        itemService.reativar(id);
        return ResponseEntity.ok(Map.of("message", "Item reativado com sucesso!"));
    }

}
