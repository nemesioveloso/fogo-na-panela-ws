package com.fogo_na_panela_ws.fogo_na_panela_ws.service;


import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Empresa salvar(Empresa empresa) {
        empresa.setSenha(passwordEncoder.encode(empresa.getSenha()));
        return empresaRepository.save(empresa);
    }
}
