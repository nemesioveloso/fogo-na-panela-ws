package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ComandaAbertaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ItemComandaRequestDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.enums.MetodoPagamento;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Comanda;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.ComandaService;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.ItemComandaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fogo-na-panela-ws/comandas")
@RequiredArgsConstructor
public class ComandaController {

    private final ComandaService comandaService;
    private final ItemComandaService itemComandaService;


    @PostMapping("/abrir")
    public ResponseEntity<Comanda> abrir(@RequestParam String mesa, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        Comanda comanda = comandaService.abrirComanda(mesa, empresaId);
        return ResponseEntity.status(201).body(comanda);
    }

    @PatchMapping("/{id}/fechar")
    public ResponseEntity<Comanda> fechar(@PathVariable Long id, @RequestParam MetodoPagamento metodo) {
        Comanda comanda = comandaService.fecharComanda(id, metodo);
        return ResponseEntity.ok(comanda);
    }

    @GetMapping("/abertas")
    public ResponseEntity<List<ComandaAbertaDTO>> listarAbertas(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        List<ComandaAbertaDTO> comandas = comandaService.listarComandasAbertas(empresaId);
        return ResponseEntity.ok(comandas);
    }

    @PostMapping("/{comandaId}/itens")
    public ResponseEntity<?> adicionarItem(@PathVariable Long comandaId,
                                           @RequestBody @Valid ItemComandaRequestDTO dto) {
        itemComandaService.adicionarItem(comandaId, dto);
        return ResponseEntity.status(201).build();
    }
}