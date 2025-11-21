package com.example.base.controller;

import com.example.base.dto.CardapioDiarioCreateDTO;
import com.example.base.dto.CardapioDiarioResponseDTO;
import com.example.base.service.CardapioDiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/cardapios")
@RequiredArgsConstructor
public class CardapioDiarioController {

    private final CardapioDiarioService cardapioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<CardapioDiarioResponseDTO> criar(
            @Valid @RequestBody CardapioDiarioCreateDTO dto) {
        return ResponseEntity.status(201).body(cardapioService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CardapioDiarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(cardapioService.listarTodosAtivos());
    }

    @GetMapping("/{dia}")
    public ResponseEntity<CardapioDiarioResponseDTO> buscarPorDia(@PathVariable String dia) {

        DayOfWeek diaEnum;
        try {
            diaEnum = DayOfWeek.valueOf(dia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.example.base.exception.BadRequestException("Dia inválido. Use MONDAY, TUESDAY, etc.");
        }

        return ResponseEntity.ok(cardapioService.buscarPorDia(diaEnum));
    }

    @GetMapping("/hoje")
    public ResponseEntity<CardapioDiarioResponseDTO> buscarHoje() {
        return ResponseEntity.ok(cardapioService.buscarCardapioDeHoje());
    }
}

