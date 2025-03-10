package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Usuario;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    public Usuario salvar(Usuario usuario, Long empresaId) {

        // Verifica se a empresa existe
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada."));

        // Verifica duplicidade de Email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um email cadastrado.");
        }

        // Verifica duplicidade de Telefone
        if (usuarioRepository.findByTelefone(usuario.getTelefone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um telefone cadastrado.");
        }

        // Vincula o usuário à empresa encontrada
        usuario.setEmpresa(empresa);

        // Salva o usuário se não houver duplicidade
        return usuarioRepository.save(usuario);
    }
}
