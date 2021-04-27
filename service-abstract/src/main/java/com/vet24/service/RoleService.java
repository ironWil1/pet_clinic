package com.vet24.service;

import com.vet24.models.Role;

import java.util.List;

public interface RoleService {
    Role getRoleById(Long id);
    List<Role> getAllRoles();
    void addRole(Role role);
    void editRole(Role role);
    void deleteRole(Long id);
}
