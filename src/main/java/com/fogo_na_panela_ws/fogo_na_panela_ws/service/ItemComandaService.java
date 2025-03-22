package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ItemComandaRequestDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Comanda;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.ItemComanda;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Produto;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ComandaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ItemComandaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ItemComandaService {

    private final ComandaRepository comandaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemComandaRepository itemComandaRepository;

    public void adicionarItem(Long comandaId, ItemComandaRequestDTO dto) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada"));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        ItemComanda item = new ItemComanda();
        item.setComanda(comanda);
        item.setProduto(produto);
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(produto.getPrecoVenda());

        itemComandaRepository.save(item);
    }
}
