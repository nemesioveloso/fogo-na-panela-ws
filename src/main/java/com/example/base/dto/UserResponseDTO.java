package com.example.base.dto;

import com.example.base.enums.Role;
import com.example.base.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String city;
    private String state;
    private boolean active;
    private Set<Role> roles;

    public static UserResponseDTO from(User u) {
        return UserResponseDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .phone(u.getPhone())
                .city(u.getCity())
                .state(u.getState())
                .active(u.isActive())
                .roles(u.getRoles())
                .build();
    }
}
