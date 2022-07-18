package com.vet24.dao.pet;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PetContactDaoImpl extends ReadWriteDaoImpl<Long, PetContact> implements PetContactDao {

    @Override
    public List<String> getAllCode() {
        Query query = manager.createQuery("SELECT petContact.code FROM PetContact AS petContact");
        return query.getResultList();
    }

    @Override
    public boolean isExistByCode(String code) {
        try {
            boolean result = false;
            if (code != null) {
                String query = "SELECT CASE WHEN (1 > 0) then true else false end FROM PetContact WHERE code = :id";
                result = manager
                        .createQuery(query, Boolean.class)
                        .setParameter("id", code)
                        .getSingleResult();
            }
            return result;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public PetContact getByCode(String code) {
        return (PetContact) manager.createQuery("FROM PetContact WHERE code = :code")
                .setParameter("code", code)
                .getSingleResult();
    }

    @Override
    public int getCountId() {
        Query query = manager.createQuery("SELECT DISTINCT (petContact.id) FROM PetContact AS petContact");
        return query.getResultList().size();
    }
}
