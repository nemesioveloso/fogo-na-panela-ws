package com.example.base.service.impl;

import com.example.base.dto.CategoriaCreateDTO;
import com.example.base.dto.CategoriaResponseDTO;
import com.example.base.exception.BadRequestException;
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
            throw new BadRequestException("Já existe uma categoria com este nome.");
        }

        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .build();

        categoriaRepository.save(categoria);

        return CategoriaResponseDTO.from(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponseDTO::from)
                .toList();
    }
}