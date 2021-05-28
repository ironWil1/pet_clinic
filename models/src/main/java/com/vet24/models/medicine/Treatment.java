package com.vet24.models.medicine;

import com.vet24.models.pet.procedure.Procedure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "procedures")
public class Treatment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    @ManyToMany(targetEntity = Procedure.class)
    @JoinTable(foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))
    private Set<Procedure> procedures  = new HashSet<>();
}
