package com.fogo_na_panela_ws.fogo_na_panela_ws.controller;

import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.ApiResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.PaginacaoResponse;
import com.fogo_na_panela_ws.fogo_na_panela_ws.dto.UsuarioDTO;
import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Usuario;
import com.fogo_na_panela_ws.fogo_na_panela_ws.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fogo-na-panela-ws/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ApiResponse> cadastrarUsuario(@Valid @RequestBody Usuario usuario, HttpServletRequest request) {
        try {
            Long empresaId = (Long) request.getAttribute("empresaId");
            if (empresaId == null) {
                return new ResponseEntity<>(new ApiResponse("Erro", "Token inválido ou ausente."), HttpStatus.UNAUTHORIZED);
            }

            usuarioService.salvar(usuario, empresaId);
            ApiResponse response = new ApiResponse("Sucesso", "Usuário cadastrado com sucesso.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            ApiResponse response = new ApiResponse("Erro", e.getReason());
            return new ResponseEntity<>(response, e.getStatusCode());
        }
    }

    @GetMapping
    public ResponseEntity<PaginacaoResponse<UsuarioDTO>> listar(HttpServletRequest request,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuarios = usuarioService.listarPorEmpresaPaginado(empresaId, pageable);

        List<UsuarioDTO> dtos = usuarios.getContent().stream()
                .map(u -> new UsuarioDTO(
                        u.getId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getTelefone(),
                        u.getCpf(),
                        u.getPermissao().name(),
                        u.getAdmissao()
                )).collect(Collectors.toList());

        PaginacaoResponse<UsuarioDTO> resposta = new PaginacaoResponse<>(
                dtos,
                usuarios.getTotalElements(),
                usuarios.getTotalPages(),
                usuarios.getNumber(),
                usuarios.getSize()
        );

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> atualizarUsuario(@PathVariable Long id,
                                                        @RequestBody @Valid Usuario usuarioAtualizado,
                                                        HttpServletRequest request) {
        try {
            Long empresaId = (Long) request.getAttribute("empresaId");
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            ApiResponse response = usuarioService.atualizar(id, usuarioAtualizado, empresaId, usuarioId);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse("Erro", e.getReason()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletarUsuario(@PathVariable Long id,
                                                      HttpServletRequest request) {
        Long empresaId = (Long) request.getAttribute("empresaId");
        Long usuarioId = (Long) request.getAttribute("usuarioId");
        usuarioService.deletar(id, empresaId, usuarioId);
        return ResponseEntity.ok(new ApiResponse("Sucesso", "Usuário deletado com sucesso."));
    }


}