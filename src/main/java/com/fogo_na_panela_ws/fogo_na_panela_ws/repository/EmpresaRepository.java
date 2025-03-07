package com.fogo_na_panela_ws.fogo_na_panela_ws.repository;


import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
