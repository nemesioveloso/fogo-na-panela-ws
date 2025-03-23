package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.DetalhamentoCaixaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.DetalheMesaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ItemMesaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.LucroPorCategoriaDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Caixa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Comanda;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.ItemComanda;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.CaixaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ComandaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ItemComandaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CaixaService {
    private final CaixaRepository caixaRepository;
    private final EmpresaRepository empresaRepository;
    private final ComandaRepository comandaRepository;
    private final ItemComandaRepository itemComandaRepository;

    public void salvar(Caixa caixa) {
        caixaRepository.save(caixa);
    }

    public Caixa abrirCaixa(Long empresaId) {
        boolean jaExiste = caixaRepository.findByEmpresaIdAndDataAberturaAndAbertoTrue(
                empresaId, LocalDate.now()).isPresent();

        if (jaExiste) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Caixa já aberto hoje.");
        }

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        Caixa caixa = new Caixa();
        caixa.setDataAbertura(LocalDate.now());
        caixa.setAberto(true);
        caixa.setEmpresa(empresa);

        return caixaRepository.save(caixa);
    }

    public Caixa buscarCaixaAberto(Long empresaId) {
        return caixaRepository.findByEmpresaIdAndDataAberturaAndAbertoTrue(empresaId, LocalDate.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Caixa não está aberto."));
    }

    public DetalhamentoCaixaDTO detalharLucroPorCategoria(Long empresaId) {
        Caixa caixa = buscarCaixaAberto(empresaId);
        List<Comanda> comandas = comandaRepository.findAllByFechadaTrueAndCaixaId(caixa.getId());

        Map<String, BigDecimal> categoriaTotal = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Comanda c : comandas) {
            List<ItemComanda> itens = itemComandaRepository.findAllByComandaId(c.getId());
            for (ItemComanda item : itens) {
                String categoria = item.getProduto().getCategoria();
                BigDecimal valor = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));

                categoriaTotal.put(categoria, categoriaTotal.getOrDefault(categoria, BigDecimal.ZERO).add(valor));
                total = total.add(valor);
            }
        }

        List<LucroPorCategoriaDTO> detalhes = categoriaTotal.entrySet().stream()
                .map(e -> new LucroPorCategoriaDTO(e.getKey(), e.getValue()))
                .toList();

        return new DetalhamentoCaixaDTO(caixa.getDataAbertura(), total, detalhes);
    }

    public List<DetalheMesaDTO> detalharComandasFechadas(Long empresaId) {
        Caixa caixa = buscarCaixaAberto(empresaId);
        List<Comanda> comandas = comandaRepository.findAllByFechadaTrueAndCaixaId(caixa.getId());

        List<DetalheMesaDTO> resposta = new ArrayList<>();

        for (Comanda comanda : comandas) {
            List<ItemComanda> itens = itemComandaRepository.findAllByComandaId(comanda.getId());

            List<ItemMesaDTO> detalheItens = itens.stream()
                    .map(i -> new ItemMesaDTO(
                            i.getProduto().getNome(),
                            i.getProduto().getCategoria(),
                            i.getQuantidade(),
                            i.getPrecoUnitario()
                    )).toList();

            BigDecimal totalMesa = detalheItens.stream()
                    .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            resposta.add(new DetalheMesaDTO(comanda.getMesa(), detalheItens, totalMesa));
        }

        return resposta;
    }

}