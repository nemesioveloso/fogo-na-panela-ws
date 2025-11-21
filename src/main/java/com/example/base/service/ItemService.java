package com.example.base.service;

import com.example.base.dto.ItemCreateDTO;
import com.example.base.dto.ItemResponseDTO;

import java.util.List;

public interface ItemService {
    ItemResponseDTO criar(ItemCreateDTO dto);
    List<ItemResponseDTO> listarTodos();
    List<ItemResponseDTO> listarPorCategoria(Long categoriaId);
}
