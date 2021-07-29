package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.Admin;

public interface AdminDao extends ReadWriteDao<Long, Admin> {
    Admin getAdminByEmail(String email);
}
