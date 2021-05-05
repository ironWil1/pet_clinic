package com.vet24.models.pet.reproduction;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
public class Reproduction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate estrusStart;

    @Column
    private LocalDate mating;

    @Column
    private LocalDate dueDate;

    @Column
    private Integer childCount;
}
