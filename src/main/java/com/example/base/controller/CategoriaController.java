package com.example.base.controller;


import com.example.base.dto.CategoriaCreateDTO;
import com.example.base.dto.CategoriaResponseDTO;
import com.example.base.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, String>> criar(@Valid @RequestBody CategoriaCreateDTO dto) {

        categoriaService.criar(dto);

        return ResponseEntity
                .status(201)
                .body(Map.of("message", "Categoria criada com sucesso!"));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodasAtivas());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaCreateDTO dto
    ) {
        return ResponseEntity.ok(categoriaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        categoriaService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Long id) {

        categoriaService.reativar(id);

        return ResponseEntity.ok(
                Map.of("message", "Categoria reativada com sucesso!")
        );
    }

}


