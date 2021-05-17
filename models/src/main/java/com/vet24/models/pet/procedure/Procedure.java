package com.vet24.models.pet.procedure;

import com.vet24.models.medicine.Medicine;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Procedure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column(name = "type", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public ProcedureType type;

    @Column
    private String medicineBatchNumber;

    @Column
    private Boolean isPeriodical;

    @Column
    private Integer periodDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToMany(mappedBy = "procedures")
    private Set<Pet> pets = new HashSet<>();

    protected Procedure(LocalDate date, ProcedureType type, String medicineBatchNumber,
                     Boolean isPeriodical, Integer periodDays, Medicine medicine) {
        this.date = date;
        this.type = type;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
        this.medicine = medicine;
    }

    protected Procedure(LocalDate date, ProcedureType type, String medicineBatchNumber,
                        Boolean isPeriodical, Integer periodDays, Medicine medicine, Set<Pet> pets) {
        this.date = date;
        this.type = type;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
        this.medicine = medicine;
        this.pets = pets;
    }
}
