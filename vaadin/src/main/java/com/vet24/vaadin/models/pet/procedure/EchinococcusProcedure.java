package com.vet24.vaadin.models.pet.procedure;

import com.vet24.vaadin.models.enums.ProcedureType;
import com.vet24.vaadin.models.medicine.Medicine;
import com.vet24.vaadin.models.pet.Pet;

import java.time.LocalDate;

public class EchinococcusProcedure extends Procedure {

    public EchinococcusProcedure() {
        super();
    }

    public EchinococcusProcedure(LocalDate date, String medicineBatchNumber, Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        super(date, ProcedureType.ECHINOCOCCUS, medicineBatchNumber, isPeriodical, periodDays, medicine, pet);
    }
}
