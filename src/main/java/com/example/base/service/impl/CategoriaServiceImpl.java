package com.example.base.service.impl;

import com.example.base.dto.CategoriaCreateDTO;
import com.example.base.dto.CategoriaResponseDTO;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.ConflictException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.Categoria;
import com.example.base.repository.CategoriaRepository;
import com.example.base.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponseDTO criar(CategoriaCreateDTO dto) {

        if (categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new ConflictException("Já existe uma categoria com este nome.");
        }

        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .ativo(true)
                .build();

        categoriaRepository.save(categoria);

        return CategoriaResponseDTO.from(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> listarTodasAtivas() {
        return categoriaRepository.findByAtivoTrue()
                .stream()
                .map(CategoriaResponseDTO::from)
                .toList();
    }

    @Override
    public CategoriaResponseDTO atualizar(Long id, CategoriaCreateDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        if (!categoria.getNome().equalsIgnoreCase(dto.getNome())
                && categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new ConflictException("Já existe uma categoria com este nome.");
        }

        categoria.setNome(dto.getNome());
        categoriaRepository.save(categoria);

        return CategoriaResponseDTO.from(categoria);
    }

    @Override
    public void inativar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        if (!categoria.isAtivo()) return;

        categoria.setAtivo(false);
        categoriaRepository.save(categoria);
    }
}