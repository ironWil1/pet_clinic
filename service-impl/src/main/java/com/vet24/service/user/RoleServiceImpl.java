package com.vet24.service.user;

import com.vet24.dao.ReadOnlyDaoImpl;
import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Role;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ReadWriteServiceImpl<Long, Role> implements RoleService {

    protected RoleServiceImpl(ReadWriteDaoImpl<Long, Role> readWriteDao) {
        super(readWriteDao);
    }
}
