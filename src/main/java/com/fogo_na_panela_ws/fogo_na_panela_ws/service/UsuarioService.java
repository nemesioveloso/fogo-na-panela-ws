package com.fogo_na_panela_ws.fogo_na_panela_ws.service;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.enums.Permissao;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Usuario;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.EmpresaRepository;
import com.fogo_na_panela_ws.fogo_na_panela_ws.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final LogAcaoService logAcaoService;

    public Usuario salvar(Usuario usuario, Long empresaId) {
        // Verifica se a empresa existe
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada."));

        // Verifica duplicidade de Email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um email cadastrado.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória.");
        }

        // Verifica duplicidade de Telefone
        if (usuarioRepository.findByTelefone(usuario.getTelefone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um telefone cadastrado.");
        }

        // Verifica duplicidade de CPF
        if (usuarioRepository.findByCpf(usuario.getCpf()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um CPF cadastrado.");
        }

        // Verifica se a permissão é válida
        if (usuario.getPermissao() == null || (!usuario.getPermissao().equals(Permissao.ADMIN) && (!usuario.getPermissao().equals(Permissao.GERENTE)) && !usuario.getPermissao().equals(Permissao.USER))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Permissão inválida. Deve ser ADMIN, USER ou GERENTE.");
        }

        // Preenche admissão com a data e hora atual
        usuario.setAdmissao(LocalDateTime.now());

        // Vincula o usuário à empresa encontrada
        usuario.setEmpresa(empresa);

        // Salva o usuário se não houver duplicidade
        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> listarPorEmpresaPaginado(Long empresaId, Pageable pageable) {
        return usuarioRepository.findAllByEmpresaId(empresaId, pageable);
    }

    public ApiResponse atualizar(Long id, Usuario atualizado, Long empresaId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (!usuario.getEmpresa().getId().equals(empresaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para editar este usuário.");
        }

        usuarioRepository.findByEmail(atualizado.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um e-mail cadastrado.");
                });

        usuarioRepository.findByTelefone(atualizado.getTelefone())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um telefone cadastrado.");
                });

        usuarioRepository.findByCpf(atualizado.getCpf())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um CPF cadastrado.");
                });

        // Registro antes da atualização
        String dadosAntigos = String.format("Nome: %s, Email: %s, Telefone: %s, CPF: %s, Permissão: %s",
                usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), usuario.getCpf(), usuario.getPermissao());

        usuario.setNome(atualizado.getNome());
        usuario.setTelefone(atualizado.getTelefone());
        usuario.setEmail(atualizado.getEmail());
        usuario.setCpf(atualizado.getCpf());
        usuario.setPermissao(atualizado.getPermissao());

        // Se uma nova senha for fornecida, atualiza a senha
        if (atualizado.getSenha() != null && !atualizado.getSenha().isBlank()) {
            usuario.setSenha(new BCryptPasswordEncoder().encode(atualizado.getSenha()));
        }

        usuarioRepository.save(usuario);

        logAcaoService.registrar(
                "Usuario",
                "ATUALIZACAO",
                "Usuário atualizado",
                dadosAntigos,
                empresaId,
                usuarioId
        );

        return new ApiResponse("Sucesso", "Usuário atualizado com sucesso.");
    }



    public void deletar(Long id, Long empresaId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (!usuario.getEmpresa().getId().equals(empresaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para excluir este usuário.");
        }

        String dadosAntigos = String.format("Nome: %s, Email: %s, Telefone: %s, CPF: %s, Permissão: %s",
                usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), usuario.getCpf(), usuario.getPermissao());

        usuarioRepository.delete(usuario);

        logAcaoService.registrar(
                "Usuario",
                "DELECAO",
                "Usuário deletado",
                dadosAntigos,
                empresaId,
                usuarioId
        );
    }


}
