package com.vet24.models.medicine;

import com.vet24.models.enums.DosageType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.GenerationType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dosage {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "dosage_size")
    private Integer dosageSize; // размер дозировки препарата

    @Column(nullable = false, name = "dosage_type")
    @Enumerated(EnumType.STRING)
    private DosageType dosageType; // форма выпуска препарата: капли, таблетки

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

}
