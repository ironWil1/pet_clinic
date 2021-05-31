package com.vet24.vaadin.models.pet.procedure;

import com.vet24.vaadin.models.enums.ProcedureType;
import com.vet24.vaadin.models.medicine.Medicine;
import com.vet24.vaadin.models.pet.Pet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"pet","medicine"})
public abstract class Procedure implements Serializable {

    private Long id;

    private LocalDate date;

    public ProcedureType type;

    private String medicineBatchNumber;

    private Boolean isPeriodical;

    private Integer periodDays;

    private Medicine medicine;

    private Pet pet;

    protected Procedure(LocalDate date, ProcedureType type, String medicineBatchNumber,
                        Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        this.date = date;
        this.type = type;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
        this.medicine = medicine;
        this.pet = pet;
    }
}
