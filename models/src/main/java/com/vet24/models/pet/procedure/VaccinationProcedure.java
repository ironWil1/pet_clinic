package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.medicine.Medicine;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue(ProcedureType.Values.VACCINATION)
public class VaccinationProcedure extends Procedure {

    public VaccinationProcedure() {
        super();
    }

    public VaccinationProcedure(LocalDate date, String medicineBatchNumber,
                                Boolean isPeriodical, Integer periodDays, Medicine medicine) {
        super(date, ProcedureType.VACCINATION, medicineBatchNumber, isPeriodical, periodDays, medicine);
    }
}
