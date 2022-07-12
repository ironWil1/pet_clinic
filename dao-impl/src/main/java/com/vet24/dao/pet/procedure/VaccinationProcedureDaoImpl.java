package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;
@Repository
public class VaccinationProcedureDaoImpl extends ReadWriteDaoImpl<Long, VaccinationProcedure> implements  VaccinationProcedureDao {

    @Override
    public boolean isPetBelongToClientByPetId(Long petId) {
        boolean result = false;

        if (petId != null) {
            String query = "SELECT CASE WHEN (count(*)>0) then true else false end" +
                    " FROM Pet" + " WHERE id" + " = :id AND client_id = " + Objects.requireNonNull(getSecurityUserOrNull()).getId();
            result = manager
                    .createQuery(query, Boolean.class)
                    .setParameter("id", petId)
                    .getSingleResult();
        }

        return result;
    }
}
