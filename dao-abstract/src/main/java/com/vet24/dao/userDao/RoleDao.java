package com.vet24.dao.userDao;

import com.vet24.models.user.Role;

import java.util.List;

public interface RoleDao {
    Role getRoleById(Long id);
    List<Role> getAllRoles();
    void addRole(Role role);
    void editRole(Role role);
    void deleteRole(Long id);
}
