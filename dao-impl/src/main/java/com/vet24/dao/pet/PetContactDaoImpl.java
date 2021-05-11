package com.vet24.dao.pet;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.PetContact;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PetContactDaoImpl extends ReadWriteDaoImpl<Long, PetContact> implements PetContactDao {

    @Override
    public List<String> getAllUniqueCode() {
        Query query = manager.createQuery("SELECT petContact.uniqCode FROM PetContact as petContact");
        return query.getResultList();
    }

    @Override
    public int getCountId() {
        Query query = manager.createQuery("SELECT count(distinct petContact.id) FROM PetContact as petContact");
        return query.getResultList().size();
    }
}
