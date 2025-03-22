package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ProdutoResponseDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Produto;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EmpresaRepository empresaRepository;

    public ProdutoResponseDTO salvar(Produto produto, Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Empresa não encontrada."));

        produto.setEmpresa(empresa);
        produto.setAdicionado(LocalDate.now());
        produto.setUltimaAlteracao(LocalDate.now());

        Produto salvo = produtoRepository.save(produto);

        return ProdutoResponseDTO.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .categoria(salvo.getCategoria())
                .precoCompra(salvo.getPrecoCompra())
                .precoVenda(salvo.getPrecoVenda())
                .estoque(salvo.getEstoque())
                .adicionado(salvo.getAdicionado().toString())
                .ultimaAlteracao(salvo.getUltimaAlteracao().toString())
                .build();
    }

    public Page<Produto> listarPorEmpresaPaginado(Long empresaId, Pageable pageable) {
        return produtoRepository.findAllByEmpresaId(empresaId, pageable);
    }
}
