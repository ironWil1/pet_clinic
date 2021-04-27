package com.vet24.dao;

import com.vet24.models.Role;

import java.util.List;

public interface RoleDao {
    Role getRoleById(Long id);
    List<Role> getAllRoles();
    void addRole(Role role);
    void editRole(Role role);
    void deleteRole(Long id);
}
