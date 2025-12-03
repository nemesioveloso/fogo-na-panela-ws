package com.example.base.service.impl;

import com.example.base.dto.ComposicaoQuentinhaCreateDTO;
import com.example.base.dto.ComposicaoQuentinhaResponseDTO;
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

        return ComposicaoQuentinhaResponseDTO.from(composicao);
    }

    @Override
    @Transactional
    public ComposicaoQuentinhaResponseDTO atualizar(Long tipoQuentinhaId, Long composicaoId, ComposicaoQuentinhaCreateDTO dto) {

        ComposicaoQuentinha composicao = composicaoRepository.findById(composicaoId)
                .orElseThrow(() -> new NotFoundException("Composição não encontrada."));

        if (!composicao.getTipoQuentinha().getId().equals(tipoQuentinhaId)) {
            throw new BadRequestException("Composição não pertence a este tipo de quentinha.");
        }

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

            if (categoria.getTipo() != TipoItem.QUENTINHA) {
                throw new BadRequestException("Categoria precisa ser do tipo QUENTINHA para composição de quentinha.");
            }

            composicao.setCategoria(categoria);
        }

        composicao.setQuantidadeObrigatoria(dto.getQuantidadeObrigatoria());
        composicao.setQuantidadeInclusa(dto.getQuantidadeInclusa());
        composicao.setContabilizaComoCarneExtra(dto.isContabilizaComoCarneExtra());

        composicaoRepository.save(composicao);

        return ComposicaoQuentinhaResponseDTO.from(composicao);
    }

    @Override
    @Transactional
    public void remover(Long tipoQuentinhaId, Long composicaoId) {
        ComposicaoQuentinha composicao = composicaoRepository.findById(composicaoId)
                .orElseThrow(() -> new NotFoundException("Composição não encontrada."));

        if (!composicao.getTipoQuentinha().getId().equals(tipoQuentinhaId)) {
            throw new BadRequestException("Composição não pertence a este tipo de quentinha.");
        }

        composicaoRepository.delete(composicao);
    }
}
