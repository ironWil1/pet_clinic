package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.Deworming;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DewormingDaoImpl extends ReadWriteDaoImpl<Long, Deworming> implements DewormingDao {
    @Override
    public boolean isExistByDewormingIdAndClientId(Long dewormingId, Long clientId) {
        String query = "SELECT CASE WHEN (count(*)>0) then true else false end" +
                " FROM Deworming d WHERE d.id = :dewormingId and d.pet.client.id = :clientId";
        return manager
                .createQuery(query, Boolean.class)
                .setParameter("dewormingId", dewormingId)
                .setParameter("clientId", clientId)
                .getSingleResult();
    }

    @Override
    public List<Deworming> getByPetId(Long petId) {
        return manager.createQuery("SELECT d FROM Deworming d WHERE d.pet.id = :petId", Deworming.class)
                .setParameter("petId", petId)
                .getResultList();
    }
}
