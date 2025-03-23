package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.LogAcao;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.LogAcaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogAcaoService {

    private final LogAcaoRepository logAcaoRepository;

    public void registrar(String entidade, String tipoAcao, String descricao, String dadosAnteriores, Long empresaId, Long usuarioId) {
        LogAcao log = LogAcao.builder()
                .entidade(entidade)
                .tipoAcao(tipoAcao)
                .descricao(descricao)
                .dadosAnteriores(dadosAnteriores)
                .dataHora(LocalDateTime.now())
                .empresaId(empresaId)
                .usuarioId(usuarioId)
                .build();

        logAcaoRepository.save(log);
    }
}