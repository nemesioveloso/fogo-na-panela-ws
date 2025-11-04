package com.example.base.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateDTO {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;

    @NotBlank
    private String fullName;

    @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", message = "CPF inválido")
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

    @Pattern(regexp = "\\d{8}|\\d{5}\\-\\d{3}", message = "CEP inválido")
    private String zipCode;
}
