package com.example.base.service;

import com.example.base.dto.UserCreateDTO;
import com.example.base.dto.UserPatchDTO;
import com.example.base.enums.Role;
import com.example.base.model.User;
import java.util.Set;

public interface UserService {
    User create(UserCreateDTO dto);
    void delete(Long id);
    User partialUpdate(Long id, UserPatchDTO dto, Long requesterId, Set<Role> roles);
}