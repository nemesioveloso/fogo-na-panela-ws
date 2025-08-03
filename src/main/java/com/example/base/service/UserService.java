package com.example.base.service;

import com.example.base.dto.UserCreateDTO;
import com.example.base.enums.Role;
import com.example.base.model.User;

import java.util.Map;
import java.util.Set;

public interface UserService {
    User create(UserCreateDTO dto);
    void delete(Long id);
    User partialUpdate(Long id, Map<String, Object> updates, Long requesterId, Set<Role> roles);

}