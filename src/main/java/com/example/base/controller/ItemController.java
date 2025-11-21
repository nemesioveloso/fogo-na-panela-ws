package com.example.base.controller;

import com.example.base.dto.ItemCreateDTO;
import com.example.base.dto.ItemResponseDTO;
import com.example.base.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponseDTO> criar(@Valid @RequestBody ItemCreateDTO dto) {
        return ResponseEntity.status(201).body(itemService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarTodos() {
        return ResponseEntity.ok(itemService.listarTodos());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ItemResponseDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(itemService.listarPorCategoria(categoriaId));
    }
}
