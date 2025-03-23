package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.MovimentacaoRequestDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Caixa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.MovimentacaoCaixa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.MovimentacaoCaixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovimentacaoCaixaService {

    private final CaixaService caixaService;
    private final MovimentacaoCaixaRepository movimentacaoCaixaRepository;

    public void adicionarMovimentacao(Long empresaId, MovimentacaoRequestDTO dto) {
        Caixa caixa = caixaService.buscarCaixaAberto(empresaId);

        // Ajusta o valor total do caixa
        switch (dto.getTipo()) {
            case ADICAO -> caixa.setValorTotalRecebido(caixa.getValorTotalRecebido().add(dto.getValor()));
            case RETIRADA -> caixa.setValorTotalRecebido(caixa.getValorTotalRecebido().subtract(dto.getValor()));
        }

        caixaService.salvar(caixa);

        MovimentacaoCaixa movimentacao = MovimentacaoCaixa.builder()
                .tipo(dto.getTipo())
                .observacao(dto.getObservacao())
                .dataHora(LocalDateTime.now())
                .valor(dto.getValor())
                .caixa(caixa)
                .build();

        movimentacaoCaixaRepository.save(movimentacao);
    }

}
