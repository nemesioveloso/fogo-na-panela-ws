package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public Empresa salvar(Empresa empresa) {

        // Verifica duplicidade de CPF/CNPJ
        if (empresaRepository.findByCpfCnpj(empresa.getCpfCnpj()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um CPF/CNPJ cadastrado.");
        }

        // Verifica duplicidade de Email
        if (empresaRepository.findByEmail(empresa.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um email cadastrado.");
        }

        // Verifica duplicidade de Telefone
        if (empresaRepository.findByTelefone(empresa.getTelefone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um telefone cadastrado.");
        }

        // Salva a empresa se não houver duplicidade
        return empresaRepository.save(empresa);
    }
}
