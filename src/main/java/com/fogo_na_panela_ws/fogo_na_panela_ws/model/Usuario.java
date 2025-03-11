package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import com.fogo_na_panela_ws.fogo_na_panela_ws.enums.Permissao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email
    @NotBlank(message = "Email é obrigatório")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(unique = true)
    private String telefone;

    @NotBlank(message = "CPF é obrigatório")
    @Column(unique = true, length = 11)
    private String cpf;  // Novo campo CPF

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Permissao permissao;  // Novo campo Permissão (ex: ADMIN, USER, GERENTE)

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @PrePersist
    @PreUpdate
    public void criptografarSenha() {
        this.senha = new BCryptPasswordEncoder().encode(senha);
    }
}
