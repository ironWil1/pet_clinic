package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureTypeEnum;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ProcedureTypeEnum.Values.EXTERNAL_PARASITE)
public class ExternalParasiteProcedure extends Procedure{
}
