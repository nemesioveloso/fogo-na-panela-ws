package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.DetalhamentoCaixaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.DetalheMesaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Caixa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.CaixaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fogo-na-panela-ws/caixa")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaService caixaService;

    @PostMapping("/abrir")
    public ResponseEntity<Caixa> abrir(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        Caixa caixa = caixaService.abrirCaixa(empresaId);
        return ResponseEntity.status(201).body(caixa);
    }

    @GetMapping("/detalhamento/lucro-categoria")
    public ResponseEntity<DetalhamentoCaixaDTO> detalharLucroPorCategoria(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        return ResponseEntity.ok(caixaService.detalharLucroPorCategoria(empresaId));
    }

    @GetMapping("/detalhamento/mesas-fechadas")
    public ResponseEntity<List<DetalheMesaDTO>> detalharComandas(HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        return ResponseEntity.ok(caixaService.detalharComandasFechadas(empresaId));
    }
}
