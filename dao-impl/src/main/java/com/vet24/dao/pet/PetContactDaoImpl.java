package com.vet24.dao.pet;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.PetContact;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PetContactDaoImpl extends ReadWriteDaoImpl<Long, PetContact> implements PetContactDao {

    @Override
    public List<String> getAllPetCode() {
        Query query = manager.createQuery("SELECT petContact.petCode FROM PetContact AS petContact");
        return query.getResultList();
    }

    @Override
    public boolean isExistByPetCode(String code) {
        try {
            PetContact petContact = (PetContact) manager.createQuery("FROM PetContact WHERE petCode = :petCode")
                    .setParameter("petCode", code)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public int getCountId() {
        Query query = manager.createQuery("SELECT DISTINCT (petContact.id) FROM PetContact AS petContact");
        return query.getResultList().size();
    }
}
