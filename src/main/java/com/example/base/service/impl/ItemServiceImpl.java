package com.example.base.service.impl;

import com.example.base.dto.ItemCreateDTO;
import com.example.base.dto.ItemResponseDTO;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.Categoria;
import com.example.base.model.Item;
import com.example.base.repository.CategoriaRepository;
import com.example.base.repository.ItemRepository;
import com.example.base.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public ItemResponseDTO criar(ItemCreateDTO dto) {

        if (itemRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BadRequestException("Já existe um item com este nome.");
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        Item item = Item.builder()
                .nome(dto.getNome())
                .categoria(categoria)
                .build();

        itemRepository.save(item);

        return ItemResponseDTO.from(item);
    }

    @Override
    public List<ItemResponseDTO> listarPorCategoria(Long categoriaId) {

        if (!categoriaRepository.existsById(categoriaId)) {
            throw new NotFoundException("Categoria não encontrada.");
        }

        return itemRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(ItemResponseDTO::from)
                .toList();
    }

    @Override
    public List<ItemResponseDTO> listarTodos() {
        return itemRepository.findAll()
                .stream()
                .map(ItemResponseDTO::from)
                .toList();
    }
}
