package com.example.base.service.impl;

import com.example.base.dto.CardapioDiarioCreateDTO;
import com.example.base.dto.CardapioDiarioResponseDTO;
import com.example.base.dto.CardapioDiarioUpdateDTO;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.ConflictException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.CardapioDiario;
import com.example.base.model.Item;
import com.example.base.repository.CardapioDiarioRepository;
import com.example.base.repository.ItemRepository;
import com.example.base.service.CardapioDiarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardapioDiarioServiceImpl implements CardapioDiarioService {

    private final CardapioDiarioRepository cardapioRepository;
    private final ItemRepository itemRepository;

    @Override
    public CardapioDiarioResponseDTO criar(CardapioDiarioCreateDTO dto) {

        if (cardapioRepository.existsByDiaSemanaAndAtivoTrue(dto.getDiaSemana())) {
            throw new ConflictException("Já existe um cardápio ativo para este dia.");
        }

        var itens = new HashSet<>(itemRepository.findAllById(dto.getItensIds()));

        if (itens.isEmpty()) {
            throw new BadRequestException("Nenhum item válido encontrado.");
        }

        CardapioDiario cardapio = CardapioDiario.builder()
                .diaSemana(dto.getDiaSemana())
                .itens(itens)
                .ativo(true)
                .build();

        cardapioRepository.save(cardapio);

        return CardapioDiarioResponseDTO.from(cardapio);
    }

    @Override
    public CardapioDiarioResponseDTO buscarPorDia(DayOfWeek dia) {
        CardapioDiario cardapio = cardapioRepository.findByDiaSemanaAndAtivoTrue(dia)
                .orElseThrow(() -> new NotFoundException("Cardápio não encontrado para este dia."));

        return CardapioDiarioResponseDTO.from(cardapio);
    }

    @Override
    public List<CardapioDiarioResponseDTO> listarTodosAtivos() {
        return cardapioRepository.findByAtivoTrue()
                .stream()
                .map(CardapioDiarioResponseDTO::from)
                .toList();
    }

    @Override
    public CardapioDiarioResponseDTO buscarCardapioDeHoje() {
        DayOfWeek hoje = LocalDate.now().getDayOfWeek();

        CardapioDiario cardapio = cardapioRepository.findByDiaSemanaAndAtivoTrue(hoje)
                .orElseThrow(() -> new NotFoundException("Cardápio de hoje não foi definido."));

        return CardapioDiarioResponseDTO.from(cardapio);
    }

    @Override
    public CardapioDiarioResponseDTO atualizar(Long id, CardapioDiarioUpdateDTO dto) {

        CardapioDiario cardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cardápio não encontrado."));

        // Atualizar dia da semana (se enviado)
        if (dto.getDiaSemana() != null) {

            // Verifica se outro cardápio ativo já existe nesse dia
            boolean existeOutro = cardapioRepository
                    .existsByDiaSemanaAndAtivoTrue(dto.getDiaSemana());

            if (existeOutro && !cardapio.getDiaSemana().equals(dto.getDiaSemana())) {
                throw new ConflictException("Já existe um cardápio ativo para este dia.");
            }

            cardapio.setDiaSemana(dto.getDiaSemana());
        }

        if (dto.getItensIds() != null) {

            var itens = new HashSet<>(itemRepository.findAllById(dto.getItensIds()));

            if (itens.isEmpty()) {
                throw new BadRequestException("Nenhum item válido encontrado.");
            }

            cardapio.setItens(itens);
        }

        cardapioRepository.save(cardapio);

        return CardapioDiarioResponseDTO.from(cardapio);
    }

    @Override
    public void inativar(Long id) {
        CardapioDiario cardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cardápio não encontrado."));

        if (!cardapio.isAtivo()) return;

        cardapio.setAtivo(false);
        cardapioRepository.save(cardapio);
    }

    @Override
    public void reativar(Long id) {
        CardapioDiario cardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cardápio não encontrado."));

        if (cardapio.isAtivo()) return;

        // Verifica conflito antes de reativar
        if (cardapioRepository.existsByDiaSemanaAndAtivoTrue(cardapio.getDiaSemana())) {
            throw new ConflictException("Já existe outro cardápio ativo para este dia.");
        }

        cardapio.setAtivo(true);
        cardapioRepository.save(cardapio);
    }


}
