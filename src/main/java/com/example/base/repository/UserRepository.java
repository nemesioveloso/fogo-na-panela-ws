package com.example.base.repository;

import com.example.base.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCpf(String cpf);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndActiveTrue(String email);
    Optional<User> findByUsernameAndActiveTrue(String username);
}
