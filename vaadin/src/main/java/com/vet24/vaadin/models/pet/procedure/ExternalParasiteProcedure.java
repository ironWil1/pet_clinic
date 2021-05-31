package com.vet24.vaadin.models.pet.procedure;

import com.vet24.vaadin.models.enums.ProcedureType;
import com.vet24.vaadin.models.medicine.Medicine;
import com.vet24.vaadin.models.pet.Pet;

import java.time.LocalDate;

public class ExternalParasiteProcedure extends Procedure {

    public ExternalParasiteProcedure() {
        super();
    }

    public ExternalParasiteProcedure(LocalDate date, String medicineBatchNumber, Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        super(date, ProcedureType.EXTERNAL_PARASITE, medicineBatchNumber, isPeriodical, periodDays, medicine, pet);
    }
}
