package com.example.base.dto;

import com.example.base.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserPatchDTO {

    @Email(message = "Email inválido")
    private String email;

    private String username;
    private String fullName;
    private String phone;
    private String city;
    private String state;
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private LocalDate birthDate;
    private String gender;
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;
    private Set<Role> roles;
}
