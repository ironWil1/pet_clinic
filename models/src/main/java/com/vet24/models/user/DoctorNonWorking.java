package com.vet24.models.user;

import com.vet24.models.enums.DayOffType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class DoctorNonWorking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;
    @Enumerated(EnumType.STRING)
    private DayOffType type;  //- enum указания причины (VACATION - отпуск, DAY_OFF - выходной, SICK_LEAVE - больничный)
    @Column
    private LocalDate date;

    public DoctorNonWorking(Doctor doctor, DayOffType type, LocalDate date) {
        this.doctor = doctor;
        this.type = type;
        this.date = date;
    }
}
