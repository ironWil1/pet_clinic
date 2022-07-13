package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.user.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

@Repository
@Slf4j
public class ExternalParasiteProcedureDaoImpl extends ReadWriteDaoImpl<Long, ExternalParasiteProcedure> implements ExternalParasiteProcedureDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ExternalParasiteProcedure> getExternalParasiteListByPetId(Long petId) {
        return entityManager.createQuery("FROM ExternalParasiteProcedure where pet_id =:pet_id", ExternalParasiteProcedure.class)
                .setParameter("pet_id", petId)
                .getResultList();
    }

    @Override
    public ExternalParasiteProcedure getExternalParasiteById(Long id) {
        return entityManager.createQuery("FROM ExternalParasiteProcedure where id =:id", ExternalParasiteProcedure.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Boolean isPetBelongToClientByPetId(Long petId) {
        if (petId == null) {
            log.info("Питомец с id {} не найден", petId);
            throw new NotFoundException("Питомец не найден");
        }

        return entityManager.createQuery("FROM Pet where id =:petId", Pet.class)
                .setParameter("petId", petId)
                .getSingleResult()
                .getClient().getId()
                .equals(getSecurityUserOrNull().getId());
    }
}