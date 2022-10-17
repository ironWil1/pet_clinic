package com.vet24.models.medicine;

import com.vet24.models.enums.DosageType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(
            mappedBy = "dosage",
            cascade = CascadeType.ALL
    )
    private List<Medicine> medicines = new ArrayList<>();

    

}
