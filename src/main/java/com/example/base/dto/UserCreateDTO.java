package com.example.base.dto;

import com.example.base.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserCreateDTO {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    @Pattern(regexp = "\\d{11}", message = "CPF inválido")
    private String cpf;

    private LocalDate birthDate;

    private String gender;

    private String phone;

    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;

    @Pattern(regexp = "\\d{8}", message = "CEP inválido")
    private String zipCode;

    private Set<Role> roles;
}
