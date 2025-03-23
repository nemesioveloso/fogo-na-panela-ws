package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.MovimentacaoRequestDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.MovimentacaoCaixaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fogo-na-panela-ws/caixa/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoCaixaController {

    private final MovimentacaoCaixaService movimentacaoCaixaService;

    @PostMapping
    public ResponseEntity<ApiResponse> adicionarMovimentacao(@RequestBody @Valid MovimentacaoRequestDTO dto,
                                                             HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        movimentacaoCaixaService.adicionarMovimentacao(empresaId, dto);
        return ResponseEntity.status(201).body(new ApiResponse("Sucesso", "Movimentação registrada com sucesso."));
    }
}