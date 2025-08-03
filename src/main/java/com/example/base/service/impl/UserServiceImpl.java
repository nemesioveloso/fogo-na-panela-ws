package com.example.base.service.impl;

import com.example.base.dto.UserCreateDTO;
import com.example.base.enums.Role;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.exception.UnauthorizedException;
import com.example.base.model.User;
import com.example.base.repository.UserRepository;
import com.example.base.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(UserCreateDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("E-mail já cadastrado.");
        }

        if (repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BadRequestException("CPF já cadastrado.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
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
                .roles(dto.getRoles() != null ? dto.getRoles() : Set.of(Role.USER))
                .build();

        return repository.save(user);
    }

    @Override
    public User partialUpdate(Long id, Map<String, Object> updates, Long requesterId, Set<Role> roles) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        boolean isAdmin = roles.contains(Role.ADMIN);
        boolean isSelf = requesterId.equals(id);

        if (!isAdmin && !isSelf) {
            throw new UnauthorizedException("Você não tem permissão para alterar este usuário.");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "email" -> user.setEmail(value.toString());
                case "username" -> user.setUsername(value.toString());
                case "fullName" -> user.setFullName(value.toString());
                case "phone" -> user.setPhone(value.toString());
                case "city" -> user.setCity(value.toString());
                case "state" -> user.setState(value.toString());
                case "zipCode" -> user.setZipCode(value.toString());
                case "street" -> user.setStreet(value.toString());
                case "number" -> user.setNumber(value.toString());
                case "complement" -> user.setComplement(value.toString());
                case "neighborhood" -> user.setNeighborhood(value.toString());
                case "birthDate" -> user.setBirthDate(LocalDate.parse(value.toString()));
                case "gender" -> user.setGender(value.toString());
                case "password" -> user.setPassword(passwordEncoder.encode(value.toString()));
                case "roles" -> {
                    if (isAdmin) {
                        @SuppressWarnings("unchecked")
                        Set<Role> roleSet = ((List<String>) value).stream()
                                .map(Role::valueOf)
                                .collect(Collectors.toSet());
                        user.setRoles(roleSet);
                    }
                }
            }
        });

        return repository.save(user);
    }


    @Override
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        user.setActive(false);
        repository.save(user);
    }

}
