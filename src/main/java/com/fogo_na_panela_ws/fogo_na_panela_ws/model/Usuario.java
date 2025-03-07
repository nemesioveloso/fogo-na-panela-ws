package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @PrePersist
    @PreUpdate
    public void criptografarSenha() {
        this.senha = new BCryptPasswordEncoder().encode(senha);
    }
}

