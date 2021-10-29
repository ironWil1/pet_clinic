package com.vet24.service.user;

import com.vet24.dao.user.AdminDao;
import com.vet24.models.user.Admin;
import com.vet24.models.user.Client;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl extends ReadWriteServiceImpl<Long, Admin> implements AdminService {

    private final AdminDao adminDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao, PasswordEncoder passwordEncoder) {
        super(adminDao);
        this.adminDao = adminDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getAdminByEmail(String email) {
        return adminDao.getAdminByEmail(email);
    }

    @Override
    @Transactional()
    public void persist(Admin admin) {
        String password = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(password);
        adminDao.persist(admin);
    }

    @Override
    @Transactional
    public Admin update(Admin admin) {
        String newPassword = admin.getPassword();
        if(passwordEncoder.upgradeEncoding(newPassword)) {
            String password = passwordEncoder.encode(newPassword);
            admin.setPassword(password);
        }
        return adminDao.update(admin);
    }

    @Override
    @Transactional
    public void persistAll(List<Admin> admins) {
        for (Admin admin : admins) {
            String password = passwordEncoder.encode(admin.getPassword());
            admin.setPassword(password);
        }
        adminDao.persistAll(admins);
    }

    @Override
    @Transactional
    public List<Admin> updateAll(List<Admin> admins) {
        for (Admin admin : admins) {
            String newPassword = admin.getPassword();
            if(passwordEncoder.upgradeEncoding(newPassword)) {
                String password = passwordEncoder.encode(newPassword);
                admin.setPassword(password);
            }
        }
        return adminDao.updateAll(admins);
    }
}
