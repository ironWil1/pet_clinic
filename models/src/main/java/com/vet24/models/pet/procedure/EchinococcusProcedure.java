package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureTypeEnum;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ProcedureTypeEnum.Values.ECHINOCOCCUS)
public class EchinococcusProcedure extends Procedure{
}
