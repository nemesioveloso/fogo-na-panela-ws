package com.example.base.service;

import com.example.base.dto.CategoriaCreateDTO;
import com.example.base.dto.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDTO criar(CategoriaCreateDTO dto);
    List<CategoriaResponseDTO> listarTodas();
}