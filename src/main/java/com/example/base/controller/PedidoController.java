package com.example.base.controller;

import com.example.base.dto.PedidoCreateDTO;
import com.example.base.dto.PedidoResponseDTO;
import com.example.base.enums.PedidoStatus;
import com.example.base.model.User;
import com.example.base.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(
            @Valid @RequestBody PedidoCreateDTO dto) {

        User usuario = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseEntity.status(201)
                .body(pedidoService.criar(dto, usuario.getId()));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        User usuario = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuario.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscar(@PathVariable Long id) {
        User usuario = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseEntity.ok(pedidoService.buscar(id, usuario.getId()));
    }

    @PostMapping("/{id}/refazer")
    public ResponseEntity<PedidoResponseDTO> refazer(@PathVariable Long id) {
        User usuario = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseEntity.status(201)
                .body(pedidoService.refazer(id, usuario.getId()));
    }

    @GetMapping("/cozinha")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<PedidoResponseDTO>> listarParaCozinha() {
        return ResponseEntity.ok(pedidoService.listarParaCozinha());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<PedidoResponseDTO> avancarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.avancarStatus(id));
    }
}

