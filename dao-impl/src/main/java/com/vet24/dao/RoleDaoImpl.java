package com.vet24.dao;

import com.vet24.models.Role;
import com.vet24.models.User;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Role getRoleById(Long id) {
        Role role = (Role) entityManager.createQuery("from Role where id =:id").setParameter("id", id).getSingleResult();
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> query =
                entityManager.createQuery("SELECT u FROM Role u", Role.class);
        return query.getResultList();
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void editRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void deleteRole(Long id) {
        entityManager.remove(getRoleById(id));
    }
}
