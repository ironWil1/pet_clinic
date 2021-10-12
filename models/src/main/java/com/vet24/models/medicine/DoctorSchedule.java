package com.vet24.models.medicine;

import com.vet24.models.user.Doctor;
import lombok.*;
import com.vet24.models.enums.WorkShift;

import javax.persistence.*;
//расписание работы докторов - указывается смена, в какю работает доктор в конкретную неделю
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkShift workShift;

    @Column
    private Integer weekNumber;

    public DoctorSchedule(Doctor doctor, WorkShift workShift, Integer weekNumber) {
        this.doctor = doctor;
        this.workShift = workShift;
        this.weekNumber = weekNumber;
    }
}
