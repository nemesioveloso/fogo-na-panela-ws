package com.example.base.service.impl;

import com.example.base.dto.ComposicaoQuentinhaCreateDTO;
import com.example.base.dto.ComposicaoQuentinhaResponseDTO;
import com.example.base.dto.ComposicaoQuentinhaUpdateDTO;
import com.example.base.enums.TipoItem;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.Categoria;
import com.example.base.model.ComposicaoQuentinha;
import com.example.base.model.TipoQuentinha;
import com.example.base.repository.CategoriaRepository;
import com.example.base.repository.ComposicaoQuentinhaRepository;
import com.example.base.repository.TipoQuentinhaRepository;
import com.example.base.service.ComposicaoQuentinhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComposicaoQuentinhaServiceImpl implements ComposicaoQuentinhaService {

    private final ComposicaoQuentinhaRepository composicaoRepository;
    private final TipoQuentinhaRepository tipoQuentinhaRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public List<ComposicaoQuentinhaResponseDTO> listarPorTipo(Long tipoQuentinhaId) {
        return composicaoRepository.findByTipoQuentinhaId(tipoQuentinhaId)
                .stream()
                .map(ComposicaoQuentinhaResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional
    public ComposicaoQuentinhaResponseDTO criar(Long tipoQuentinhaId, ComposicaoQuentinhaCreateDTO dto) {

        TipoQuentinha tipo = tipoQuentinhaRepository.findById(tipoQuentinhaId)
                .orElseThrow(() -> new NotFoundException("Tipo de quentinha não encontrado."));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        if (categoria.getTipo() != TipoItem.QUENTINHA) {
            throw new BadRequestException("Categoria precisa ser do tipo QUENTINHA para composição de quentinha.");
        }

        if (dto.getQuantidadeInclusa() < 0 || dto.getQuantidadeObrigatoria() < 0) {
            throw new BadRequestException("Quantidades não podem ser negativas.");
        }

        ComposicaoQuentinha composicao = ComposicaoQuentinha.builder()
                .tipoQuentinha(tipo)
                .categoria(categoria)
                .quantidadeObrigatoria(dto.getQuantidadeObrigatoria())
                .quantidadeInclusa(dto.getQuantidadeInclusa())
                .contabilizaComoCarneExtra(dto.isContabilizaComoCarneExtra())
                .build();

        composicaoRepository.save(composicao);
        recalcularQtdCarnesInclusas(tipo);
        return ComposicaoQuentinhaResponseDTO.from(composicao);
    }

    @Override
    @Transactional
    public ComposicaoQuentinhaResponseDTO atualizar(Long tipoQuentinhaId, Long composicaoId, ComposicaoQuentinhaUpdateDTO dto) {

        ComposicaoQuentinha composicao = composicaoRepository.findById(composicaoId)
                .orElseThrow(() -> new NotFoundException("Composição não encontrada."));

        if (!composicao.getTipoQuentinha().getId().equals(tipoQuentinhaId)) {
            throw new BadRequestException("Composição não pertence a este tipo de quentinha.");
        }

        // Categoria opcional
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

            if (categoria.getTipo() != TipoItem.QUENTINHA) {
                throw new BadRequestException("Categoria precisa ser do tipo QUENTINHA para composição de quentinha.");
            }

            composicao.setCategoria(categoria);
        }

        // Quantidade obrigatória opcional
        if (dto.getQuantidadeObrigatoria() != null) {
            if (dto.getQuantidadeObrigatoria() < 0) {
                throw new BadRequestException("Quantidade obrigatória não pode ser negativa.");
            }
            composicao.setQuantidadeObrigatoria(dto.getQuantidadeObrigatoria());
        }

        // Quantidade inclusa opcional
        if (dto.getQuantidadeInclusa() != null) {
            if (dto.getQuantidadeInclusa() < 0) {
                throw new BadRequestException("Quantidade inclusa não pode ser negativa.");
            }
            composicao.setQuantidadeInclusa(dto.getQuantidadeInclusa());
        }

        // Flag contabilizaComoCarneExtra opcional
        if (dto.getContabilizaComoCarneExtra() != null) {
            composicao.setContabilizaComoCarneExtra(dto.getContabilizaComoCarneExtra());
        }

        composicaoRepository.save(composicao);

        recalcularQtdCarnesInclusas(composicao.getTipoQuentinha());

        return ComposicaoQuentinhaResponseDTO.from(composicao);
    }


    @Override
    @Transactional
    public void remover(Long tipoQuentinhaId, Long composicaoId) {
        ComposicaoQuentinha composicao = composicaoRepository.findById(composicaoId)
                .orElseThrow(() -> new NotFoundException("Composição não encontrada."));

        TipoQuentinha tipo = composicao.getTipoQuentinha();

        if (!tipo.getId().equals(tipoQuentinhaId)) {
            throw new BadRequestException("Composição não pertence a este tipo de quentinha.");
        }

        composicaoRepository.delete(composicao);
        recalcularQtdCarnesInclusas(tipo);
    }


    private void recalcularQtdCarnesInclusas(TipoQuentinha tipo) {

        // Busca todas as composições desse tipo
        var composicoes = composicaoRepository.findByTipoQuentinhaId(tipo.getId());

        // Soma apenas as que contam como carne extra
        int totalInclusas = composicoes.stream()
                .filter(ComposicaoQuentinha::isContabilizaComoCarneExtra)
                .mapToInt(ComposicaoQuentinha::getQuantidadeInclusa)
                .sum();

        tipo.setQtdCarnesInclusas(totalInclusas);
        tipoQuentinhaRepository.save(tipo);
    }
}
