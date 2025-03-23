package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ProdutoResponseDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Produto;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EmpresaRepository empresaRepository;
    private final LogAcaoService logAcaoService;

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

    @Transactional
    public Produto atualizar(Long id, Produto novoProduto, Long empresaId, Long usuarioId) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));

        if (!existente.getEmpresa().getId().equals(empresaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado.");
        }

        String dadosAntigos = existente.toString();

        existente.setNome(novoProduto.getNome());
        existente.setCategoria(novoProduto.getCategoria());
        existente.setPrecoCompra(novoProduto.getPrecoCompra());
        existente.setPrecoVenda(novoProduto.getPrecoVenda());
        existente.setEstoque(novoProduto.getEstoque());
        existente.setUltimaAlteracao(LocalDate.now());

        Produto atualizado = produtoRepository.save(existente);

        logAcaoService.registrar(
                "Produto",
                "ALTERACAO",
                "Produto atualizado",
                dadosAntigos,
                empresaId,
                usuarioId // 👈 novo parâmetro adicionado
        );

        return atualizado;
    }

    public void deletar(Long id, Long empresaId, Long usuarioId) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));

        if (!produto.getEmpresa().getId().equals(empresaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este produto.");
        }

        produtoRepository.delete(produto);
        logAcaoService.registrar("Produto", "DELECAO",
                "Produto removido", produto.toString(), empresaId, usuarioId);
    }

}
