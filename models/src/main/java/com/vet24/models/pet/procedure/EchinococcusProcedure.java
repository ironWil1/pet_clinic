package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.medicine.Medicine;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ECHINOCOCCUS")
public class EchinococcusProcedure extends Procedure {

    public EchinococcusProcedure() {
        super();
    }

    public EchinococcusProcedure(LocalDate date, String medicineBatchNumber,
                                 Boolean isPeriodical, Integer periodDays, Medicine medicine) {
        super(date, ProcedureType.ECHINOCOCCUS, medicineBatchNumber, isPeriodical, periodDays, medicine);
    }
}
