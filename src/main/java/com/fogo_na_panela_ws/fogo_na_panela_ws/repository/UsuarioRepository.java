package com.fogo_na_panela_ws.fogo_na_panela_ws.repository;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
