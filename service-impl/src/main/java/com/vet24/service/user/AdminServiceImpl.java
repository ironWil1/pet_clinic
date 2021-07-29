package com.vet24.service.user;

import com.vet24.dao.user.AdminDao;
import com.vet24.models.user.Admin;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl extends ReadWriteServiceImpl<Long, Admin> implements AdminService {

    private final AdminDao adminDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao) {
        super(adminDao);
        this.adminDao = adminDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getAdminByEmail(String email) {
        return adminDao.getAdminByEmail(email);
    }
}
