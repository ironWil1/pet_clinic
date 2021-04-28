package com.vet24.service.userServiceImpl;

import com.vet24.dao.userDao.RoleDao;
import com.vet24.models.user.Role;
import com.vet24.service.userService.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;




@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Transactional
    @Override
    public Role getRoleById(Long id) {
        return roleDao.getRoleById(id);
    }

    @Transactional
    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Transactional
    @Override
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    @Transactional
    @Override
    public void editRole(Role role) {
        roleDao.editRole(role);
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        roleDao.deleteRole(id);
    }
}
