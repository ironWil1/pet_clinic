package com.vet24.dao.pet.reproduction;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.reproduction.Reproduction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReproductionDaoImpl extends ReadWriteDaoImpl<Long, Reproduction> implements ReproductionDao {
    @Override
    public List<Reproduction> getAllByPetId(Long petId) {

        return manager.createQuery("SELECT r FROM Reproduction r WHERE r.pet.id = :petId",Reproduction.class)
                .setParameter("petId",petId)
                .getResultList();
    }
}
