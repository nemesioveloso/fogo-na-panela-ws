package com.example.base.service;

import com.example.base.dto.CategoriaCreateDTO;
import com.example.base.dto.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    void criar(CategoriaCreateDTO dto);
    List<CategoriaResponseDTO> listarTodasAtivas();
    CategoriaResponseDTO atualizar(Long id, CategoriaCreateDTO dto);
    void inativar(Long id);
    void reativar(Long id);
}