package com.fogo_na_panela_ws.fogo_na_panela_ws.repository;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCpfCnpj(String cpfCnpj);

    Optional<Empresa> findByEmail(String email);

    Optional<Empresa> findByTelefone(String telefone);
}
