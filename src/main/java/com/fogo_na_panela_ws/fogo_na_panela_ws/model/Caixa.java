package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "caixas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caixa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataAbertura;

    private Boolean aberto = true;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
