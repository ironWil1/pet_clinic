package com.vet24.service.user;

import com.vet24.models.user.Admin;
import com.vet24.service.ReadWriteService;

public interface AdminService extends ReadWriteService<Long, Admin> {
    Admin getAdminByEmail(String email);
}
