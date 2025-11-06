package com.example.base.service.impl;

import com.example.base.dto.UserCreateDTO;
import com.example.base.dto.UserPatchDTO;
import com.example.base.enums.Role;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.ConflictException;
import com.example.base.exception.NotFoundException;
import com.example.base.exception.UnauthorizedException;
import com.example.base.model.User;
import com.example.base.repository.UserRepository;
import com.example.base.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(UserCreateDTO dto) {

        if (dto.getCpf() != null) {
            dto.setCpf(dto.getCpf().replaceAll("\\D", ""));
        }

        String email = dto.getEmail().trim().toLowerCase(Locale.ROOT);
        String username = dto.getUsername().trim().toLowerCase(Locale.ROOT);

        if (repository.findByEmail(email).isPresent()) {
            throw new ConflictException("E-mail já cadastrado.");
        }

        if (dto.getCpf() != null && repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BadRequestException("CPF já cadastrado.");
        }

        if (repository.findByUsername(username).isPresent()) {
            throw new BadRequestException("Nome de usuário já cadastrado.");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .cpf(dto.getCpf())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .street(dto.getStreet())
                .number(dto.getNumber())
                .complement(dto.getComplement())
                .neighborhood(dto.getNeighborhood())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .active(true)
                .build();

        if (repository.count() == 0) {
            user.setRoles(Set.of(Role.ADMIN));
            log.warn("🚨 Primeiro usuário do sistema! Definido como ADMIN: {}", username);
        } else {
            user.setRoles(Set.of(Role.CUSTOMER));
        }

        log.info("✅ Usuário criado com sucesso: {}", username);
        return repository.save(user);
    }

    @Override
    public User partialUpdate(Long id, UserPatchDTO dto, Long requesterId, Set<Role> roles) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        boolean isAdmin = roles.contains(Role.ADMIN);
        boolean isSelf = requesterId.equals(id);

        if (!isAdmin && !isSelf) {
            throw new UnauthorizedException("Você não tem permissão para alterar este usuário.");
        }

        if (dto.getEmail() != null) {
            String email = dto.getEmail().trim().toLowerCase(Locale.ROOT);
            if (!email.equals(user.getEmail()) && repository.findByEmail(email).isPresent()) {
                throw new ConflictException("E-mail já cadastrado.");
            }
            user.setEmail(email);
        }

        if (dto.getUsername() != null) {
            String username = dto.getUsername().trim().toLowerCase(Locale.ROOT);
            if (!username.equals(user.getUsername())
                    && repository.findByUsername(username).isPresent()) {
                throw new ConflictException("Nome de usuário já cadastrado.");
            }
            user.setUsername(username);
        }

        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (isAdmin && dto.getRoles() != null) {
            user.setRoles(dto.getRoles());
        }

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getCity() != null) user.setCity(dto.getCity());
        if (dto.getState() != null) user.setState(dto.getState());
        if (dto.getZipCode() != null) user.setZipCode(dto.getZipCode());
        if (dto.getStreet() != null) user.setStreet(dto.getStreet());
        if (dto.getNumber() != null) user.setNumber(dto.getNumber());
        if (dto.getComplement() != null) user.setComplement(dto.getComplement());
        if (dto.getNeighborhood() != null) user.setNeighborhood(dto.getNeighborhood());
        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate());
        if (dto.getGender() != null) user.setGender(dto.getGender());

        log.info("✅ Usuário atualizado: {} (ID: {}) por ID: {}", user.getUsername(), id, requesterId);
        return repository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        if (!user.isActive()) {
            throw new BadRequestException("Usuário já está inativado.");
        }

        user.setActive(false);
        repository.save(user);

        log.warn("⚠️ Usuário inativado: {}", user.getUsername());
    }
}

