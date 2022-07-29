package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import org.springframework.stereotype.Repository;

@Repository
public class ExternalParasiteProcedureDaoImpl extends ReadWriteDaoImpl<Long, ExternalParasiteProcedure> implements ExternalParasiteProcedureDao {

    @Override
    public boolean isExistByIdAndClientId(Long id, Long clientId) {
        String query = "SELECT CASE WHEN (count(*)>0) THEN TRUE ELSE FALSE END" +
                " FROM ExternalParasiteProcedure e WHERE e.id = :id and e.pet.client.id = :clientId";
        return manager
                .createQuery(query, Boolean.class)
                .setParameter("id", id)
                .setParameter("clientId", clientId)
                .getSingleResult();
    }
}