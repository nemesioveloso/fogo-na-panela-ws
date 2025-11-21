package com.example.base.service.impl;

import com.example.base.dto.ItemCreateDTO;
import com.example.base.dto.ItemResponseDTO;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.ConflictException;
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
            throw new ConflictException("Já existe um item com este nome.");
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        Item item = Item.builder()
                .nome(dto.getNome())
                .categoria(categoria)
                .ativo(true)
                .build();

        itemRepository.save(item);

        return ItemResponseDTO.from(item);
    }

    @Override
    public List<ItemResponseDTO> listarTodosAtivos() {
        return itemRepository.findByAtivoTrue()
                .stream()
                .map(ItemResponseDTO::from)
                .toList();
    }

    @Override
    public List<ItemResponseDTO> listarPorCategoria(Long categoriaId) {

        if (!categoriaRepository.existsById(categoriaId)) {
            throw new NotFoundException("Categoria não encontrada.");
        }

        return itemRepository.findByCategoriaIdAndAtivoTrue(categoriaId)
                .stream()
                .map(ItemResponseDTO::from)
                .toList();
    }

    @Override
    public ItemResponseDTO atualizar(Long id, ItemCreateDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item não encontrado."));

        if (!item.getNome().equalsIgnoreCase(dto.getNome())
                && itemRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new ConflictException("Já existe um item com este nome.");
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        item.setNome(dto.getNome());
        item.setCategoria(categoria);

        itemRepository.save(item);

        return ItemResponseDTO.from(item);
    }

    @Override
    public void inativar(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item não encontrado."));

        if (!item.isAtivo()) return;

        item.setAtivo(false);
        itemRepository.save(item);
    }
}
