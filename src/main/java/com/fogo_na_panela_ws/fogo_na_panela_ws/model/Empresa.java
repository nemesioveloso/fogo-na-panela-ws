package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CPF ou CNPJ é obrigatório")
    private String cpfCnpj;

    private String endereco;

    private String dataNascimento;

    private String cep;

    private String telefone;

    private String razaoSocial;

    @Email(message = "Email inválido")
    private String email;

    private String estado;

    private String cidade;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @PrePersist
    @PreUpdate
    public void criptografarSenha() {
        this.senha = new BCryptPasswordEncoder().encode(senha);
    }
}
