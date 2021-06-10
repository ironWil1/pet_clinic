package com.vet24.models.medicine;

import com.vet24.models.pet.procedure.Procedure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Treatment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Diagnosis diagnosis;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Procedure> procedureSet;

}
