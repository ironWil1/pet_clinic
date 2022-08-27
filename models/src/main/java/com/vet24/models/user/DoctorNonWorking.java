package com.vet24.models.user;

import com.vet24.models.enums.DayOffType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

//Учет нерабочих дней.
@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DoctorNonWorking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User doctor;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private DayOffType type;  //- enum указания причины (VACATION - отпуск, DAY_OFF - выходной, SICK_LEAVE - больничный)

    @EqualsAndHashCode.Include
    private LocalDate date;

    public DoctorNonWorking(User doctor, DayOffType type, LocalDate date) {
        this.doctor = doctor;
        this.type = type;
        this.date = date;
    }
}
