package com.vet24.service.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ReadWriteServiceImpl<RoleNameEnum, Role> implements RoleService {

    public RoleServiceImpl(ReadWriteDaoImpl<RoleNameEnum, Role> readWriteDao) {
        super(readWriteDao);
    }
}
