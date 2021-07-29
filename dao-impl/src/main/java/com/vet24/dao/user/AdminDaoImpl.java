package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Admin;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class AdminDaoImpl extends ReadWriteDaoImpl<Long, Admin> implements AdminDao {
    @Override
    public Admin getAdminByEmail(String email) {
        try {
            return manager.createQuery("SELECT adm FROM Client adm WHERE adm.email =:email", Admin.class)
                    .setParameter("email", email).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
