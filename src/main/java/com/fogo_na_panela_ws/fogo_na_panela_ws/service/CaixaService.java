package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Caixa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.CaixaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CaixaService {
    private final CaixaRepository caixaRepository;
    private final EmpresaRepository empresaRepository;

    public Caixa abrirCaixa(Long empresaId) {
        boolean jaExiste = caixaRepository.findByEmpresaIdAndDataAberturaAndAbertoTrue(
                empresaId, LocalDate.now()).isPresent();

        if (jaExiste) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Caixa já aberto hoje.");
        }

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        Caixa caixa = new Caixa();
        caixa.setDataAbertura(LocalDate.now());
        caixa.setAberto(true);
        caixa.setEmpresa(empresa);

        return caixaRepository.save(caixa);
    }

    public Caixa buscarCaixaAberto(Long empresaId) {
        return caixaRepository.findByEmpresaIdAndDataAberturaAndAbertoTrue(empresaId, LocalDate.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Caixa não está aberto."));
    }
}