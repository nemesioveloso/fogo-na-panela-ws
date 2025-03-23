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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final CaixaService caixaService;
    private final ItemComandaRepository itemComandaRepository;
    private final LogAcaoService logAcaoService;

    public Comanda abrirComanda(String mesa, Long empresaId) {
        Caixa caixa = caixaService.buscarCaixaAberto(empresaId);

        Comanda comanda = new Comanda();
        comanda.setMesa(mesa);
        comanda.setCaixa(caixa);
        comanda.setFechada(false);

        return comandaRepository.save(comanda);
    }

    public Comanda fecharComanda(Long comandaId, MetodoPagamento metodoPagamento, Long usuarioId) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada"));

        if (comanda.getFechada()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Comanda já está fechada.");
        }

        List<ItemComanda> itens = itemComandaRepository.findAllByComandaId(comandaId);

        BigDecimal total = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Caixa caixa = comanda.getCaixa();
        caixa.setValorTotalRecebido(caixa.getValorTotalRecebido().add(total));
        caixaService.salvar(caixa);

        comanda.setMetodoPagamento(metodoPagamento);
        comanda.setFechada(true);
        Comanda salva = comandaRepository.save(comanda);

        logAcaoService.registrar(
                "Comanda",
                "Fechamento",
                "Comanda fechada",
                "Comanda ID: " + comandaId + ", Mesa: " + comanda.getMesa() + ", Total: " + total + ", Pagamento: " + metodoPagamento,
                comanda.getCaixa().getEmpresa().getId(),
                usuarioId
        );

        return salva;
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

            BigDecimal total = detalhes.stream()
                    .map(d -> d.getPrecoUnitario().multiply(BigDecimal.valueOf(d.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            resultado.add(new ComandaAbertaDTO(comanda.getId(), "Aberta", total, detalhes));
        }

        return resultado;
    }
}
