package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Caixa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.CaixaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
