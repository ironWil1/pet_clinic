package com.vet24.models.pet.procedure;

import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "procedure_vaccination")
public class VaccinationProcedure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private String medicineBatchNumber;

    @Column
    private Boolean isPeriodical;

    @Column
    private Integer periodDays;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pet pet;

    public VaccinationProcedure(LocalDate date, String medicineBatchNumber,
                                Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        this.date = date;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
        this.medicine = medicine;
        this.pet = pet;
    }

}
