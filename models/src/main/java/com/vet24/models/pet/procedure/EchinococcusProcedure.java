package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ProcedureType.Values.ECHINOCOCCUS)
public class EchinococcusProcedure extends Procedure{
}
