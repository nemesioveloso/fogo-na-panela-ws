package com.example.base.controller;

import com.example.base.dto.DishCreateDTO;
import com.example.base.dto.DishUpdateDTO;
import com.example.base.dto.DishResponseDTO;
import com.example.base.enums.DishCategory;
import com.example.base.exception.BadRequestException;
import com.example.base.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody DishCreateDTO dto) {
        dishService.create(dto);
        log.info("🍽️ Novo prato criado: {}", dto.getName());
        return ResponseEntity.status(201)
                .body(Map.of("message", "Prato criado com sucesso!"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, String>> update(
            @PathVariable Long id,
            @Valid @RequestBody DishUpdateDTO dto) {

        dishService.update(id, dto);
        log.info("♻️ Prato atualizado (ID: {})", id);
        return ResponseEntity.ok(Map.of("message", "Prato atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        dishService.delete(id);
        log.warn("⚠️ Prato inativado ID: {}", id);
        return ResponseEntity.ok(Map.of("message", "Prato inativado com sucesso"));
    }

    @GetMapping
    public ResponseEntity<List<DishResponseDTO>> listAll() {
        List<DishResponseDTO> dishes = dishService.listAllActive().stream()
                .map(DishResponseDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<DishResponseDTO>> listByCategory(@PathVariable String category) {
        DishCategory categoryEnum;
        try {
            categoryEnum = DishCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Categoria inválida.");
        }

        List<DishResponseDTO> dishes = dishService.listByCategory(categoryEnum).stream()
                .map(DishResponseDTO::from)
                .toList();

        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<DishResponseDTO>> listAllAdmin() {
        List<DishResponseDTO> dishes = dishService.listAllActive().stream()
                .map(DishResponseDTO::from)
                .collect(Collectors.toList());
        log.info("👨‍🍳 Listagem administrativa de pratos: {} itens", dishes.size());
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<DishResponseDTO>> listAllAdminFull() {
        List<DishResponseDTO> dishes = dishService.listAll();
        return ResponseEntity.ok(dishes);
    }
}
