package com.fogo_na_panela_ws.fogo_na_panela_ws.repository;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    Optional<Caixa> findByEmpresaIdAndDataAberturaAndAbertoTrue(Long empresaId, LocalDate data);
}