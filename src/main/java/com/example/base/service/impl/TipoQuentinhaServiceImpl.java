package com.example.base.service.impl;

import com.example.base.dto.TipoQuentinhaCreateDTO;
import com.example.base.dto.TipoQuentinhaDTO;
import com.example.base.dto.TipoQuentinhaUpdateDTO;
import com.example.base.exception.NotFoundException;
import com.example.base.model.TipoQuentinha;
import com.example.base.repository.TipoQuentinhaRepository;
import com.example.base.service.TipoQuentinhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoQuentinhaServiceImpl implements TipoQuentinhaService {

    private final TipoQuentinhaRepository repository;

    @Override
    @Transactional
    public TipoQuentinhaDTO criar(TipoQuentinhaCreateDTO dto) {

        TipoQuentinha tipo = TipoQuentinha.builder()
                .nome(dto.nome())
                .qtdCarnesInclusas(dto.qtdCarnesInclusas())
                .precoBase(dto.precoBase())
                .precoCarneExtra(dto.precoCarneExtra())
                .ativo(true)
                .build();

        repository.save(tipo);

        return TipoQuentinhaDTO.from(tipo);
    }

    @Override
    @Transactional
    public TipoQuentinhaDTO atualizarParcial(Long id, TipoQuentinhaUpdateDTO dto) {

        TipoQuentinha tipo = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de quentinha não encontrado."));

        if (dto.nome() != null)
            tipo.setNome(dto.nome());

        if (dto.qtdCarnesInclusas() != null)
            tipo.setQtdCarnesInclusas(dto.qtdCarnesInclusas());

        if (dto.precoBase() != null)
            tipo.setPrecoBase(dto.precoBase());

        if (dto.precoCarneExtra() != null)
            tipo.setPrecoCarneExtra(dto.precoCarneExtra());

        if (dto.ativo() != null)
            tipo.setAtivo(dto.ativo());

        repository.save(tipo);

        return TipoQuentinhaDTO.from(tipo);
    }

    @Override
    @Transactional
    public void inativar(Long id) {

        TipoQuentinha tipo = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de quentinha não encontrado."));

        tipo.setAtivo(false);
        repository.save(tipo);
    }

    @Override
    public TipoQuentinhaDTO buscar(Long id) {
        return repository.findById(id)
                .map(TipoQuentinhaDTO::from)
                .orElseThrow(() -> new NotFoundException("Tipo de quentinha não encontrado."));
    }

    @Override
    public List<TipoQuentinhaDTO> listarTodosAtivos() {
        return repository.findByAtivoTrue()
                .stream()
                .map(TipoQuentinhaDTO::from)
                .toList();
    }

    @Override
    public List<TipoQuentinhaDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(TipoQuentinhaDTO::from)
                .toList();
    }
}
