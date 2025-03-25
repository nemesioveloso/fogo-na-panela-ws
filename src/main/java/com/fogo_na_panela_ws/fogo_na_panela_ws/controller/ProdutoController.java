package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ProdutoDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.PaginacaoResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Produto;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.ProdutoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fogo-na-panela-ws/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ApiResponse> cadastrar(@Valid @RequestBody Produto produto,
                                                 HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        produtoService.salvar(produto, empresaId);
        return ResponseEntity.status(201)
                .body(new ApiResponse("Sucesso", String.format("%s cadastrado(a) com sucesso", produto.getNome())));
    }

    @GetMapping
    public ResponseEntity<PaginacaoResponse<ProdutoDTO>> listar(HttpServletRequest request,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        Pageable pageable = PageRequest.of(page, size);
        Page<Produto> produtos = produtoService.listarPorEmpresaPaginado(empresaId, pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<ProdutoDTO> dtos = produtos.getContent().stream()
                .map(p -> new ProdutoDTO(
                        p.getId(),
                        p.getNome(),
                        p.getCategoria(),
                        p.getPrecoCompra(),
                        p.getPrecoVenda(),
                        p.getEstoque(),
                        p.getAdicionado().format(formatter),
                        p.getUltimaAlteracao().format(formatter)
                )).collect(Collectors.toList());

        PaginacaoResponse<ProdutoDTO> resposta = new PaginacaoResponse<>(
                dtos,
                produtos.getTotalElements(),
                produtos.getTotalPages(),
                produtos.getNumber(),
                produtos.getSize()
        );

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> atualizarProduto(@PathVariable Long id,
                                                        @Valid @RequestBody Produto produtoAtualizado,
                                                        HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        Long usuarioId = (Long) request.getAttribute("usuarioId");

        produtoService.atualizar(id, produtoAtualizado, empresaId, usuarioId);

        return ResponseEntity.ok(new ApiResponse("Sucesso", String.format("%s atualizado(a) com sucesso", produtoAtualizado.getNome())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletar(@PathVariable Long id, HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        Long usuarioId = (Long) request.getAttribute("usuarioId");
        produtoService.deletar(id, empresaId, usuarioId);
        return ResponseEntity.ok(new ApiResponse("Sucesso", "Produto deletado com sucesso."));
    }


}