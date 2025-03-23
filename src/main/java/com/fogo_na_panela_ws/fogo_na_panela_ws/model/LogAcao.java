package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogAcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entidade;
    private String tipoAcao;
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String dadosAnteriores;

    private LocalDateTime dataHora;

    private Long empresaId;
    private Long usuarioId;
}
