package com.vet24.service.user;

import com.vet24.dao.user.ManagerDao;
import com.vet24.models.user.Manager;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerServiceImpl extends ReadWriteServiceImpl<Long, Manager> implements ManagerService {

    private final ManagerDao managerDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ManagerServiceImpl(ManagerDao  managerDao, PasswordEncoder passwordEncoder) {
        super(managerDao);
        this.managerDao = managerDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional()
    public void persist(Manager manager) {
        String password = passwordEncoder.encode(manager.getPassword());
        manager.setPassword(password);
        managerDao.persist(manager);
    }

    @Override
    @Transactional
    public Manager update(Manager manager) {
        String newPassword = manager.getPassword();
        if(passwordEncoder.upgradeEncoding(newPassword)) {
            String password = passwordEncoder.encode(newPassword);
            manager.setPassword(password);
        }
        return managerDao.update(manager);
    }

    @Override
    @Transactional
    public void persistAll(List<Manager> managers) {
        for (Manager manager : managers) {
            String password = passwordEncoder.encode(manager.getPassword());
            manager.setPassword(password);
        }
        managerDao.persistAll(managers);
    }

    @Override
    @Transactional
    public List<Manager> updateAll(List<Manager> managers) {
        for (Manager manager : managers) {
            String newPassword = manager.getPassword();
            if(passwordEncoder.upgradeEncoding(newPassword)) {
                String password = passwordEncoder.encode(newPassword);
                manager.setPassword(password);
            }
        }
        return managerDao.updateAll(managers);
    }
}
