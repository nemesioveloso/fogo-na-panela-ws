package com.example.base.service.impl;

import com.example.base.enums.Role;
import com.example.base.model.User;
import com.example.base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndActiveTrue(usernameOrEmail)
                .or(() -> userRepository.findByUsernameAndActiveTrue(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado ou inativo"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getRoles().stream()
                                .map(Role::asAuthority)
                                .toArray(String[]::new)
                )
                .accountLocked(!user.isAccountNonLocked())
                .disabled(!user.isEnabled())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .accountExpired(!user.isAccountNonExpired())
                .build();
    }

}
