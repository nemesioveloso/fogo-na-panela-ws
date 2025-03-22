package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ComandaAbertaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ItemDetalheDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.enums.MetodoPagamento;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Caixa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Comanda;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.ItemComanda;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ComandaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ItemComandaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComandaService {
    private final ComandaRepository comandaRepository;
    private final CaixaService caixaService;
    private final ItemComandaRepository itemComandaRepository;

    public Comanda abrirComanda(String mesa, Long empresaId) {
        Caixa caixa = caixaService.buscarCaixaAberto(empresaId);

        Comanda comanda = new Comanda();
        comanda.setMesa(mesa);
        comanda.setCaixa(caixa);
        comanda.setFechada(false);

        return comandaRepository.save(comanda);
    }

    public Comanda fecharComanda(Long comandaId, MetodoPagamento metodoPagamento) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada"));

        if (comanda.getFechada()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Comanda já está fechada.");
        }

        comanda.setMetodoPagamento(metodoPagamento);
        comanda.setFechada(true);

        return comandaRepository.save(comanda);
    }

    public List<ComandaAbertaDTO> listarComandasAbertas(Long empresaId) {
        Caixa caixa = caixaService.buscarCaixaAberto(empresaId);

        List<Comanda> comandas = comandaRepository.findAllByFechadaFalseAndCaixaId(caixa.getId());
        List<ComandaAbertaDTO> resultado = new ArrayList<>();

        for (Comanda comanda : comandas) {
            List<ItemComanda> itens = itemComandaRepository.findAllByComandaId(comanda.getId());

            List<ItemDetalheDTO> detalhes = itens.stream()
                    .map(item -> new ItemDetalheDTO(
                            item.getProduto().getCategoria(),
                            item.getProduto().getNome(),
                            item.getQuantidade(),
                            item.getPrecoUnitario()
                    )).toList();

            double total = detalhes.stream()
                    .mapToDouble(d -> d.getQuantidade() * d.getPrecoUnitario())
                    .sum();

            resultado.add(new ComandaAbertaDTO(comanda.getId(), "Aberta", total, detalhes));
        }

        return resultado;
    }
}