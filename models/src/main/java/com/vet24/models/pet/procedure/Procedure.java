package com.vet24.models.pet.procedure;

import lombok.Data;

import com.vet24.models.enums.ProcedureTypeEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Procedure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column(nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public ProcedureTypeEnum type;

    @Column
    private String medicineBatchNumber;

    @Column
    private Boolean isPeriodical;

    @Column
    private Integer periodDays;

//    @Column
//    private Medicine medicine;
}
