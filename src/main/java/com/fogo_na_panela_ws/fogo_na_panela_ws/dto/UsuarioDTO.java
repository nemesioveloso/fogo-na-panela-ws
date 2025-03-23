package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String permissao;
}
