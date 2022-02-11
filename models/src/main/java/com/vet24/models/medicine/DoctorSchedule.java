package com.vet24.models.medicine;

import com.vet24.models.user.Doctor;
import com.vet24.models.validation.FirstDayOfWeek;
import lombok.*;
import com.vet24.models.enums.WorkShift;
import org.hibernate.validator.internal.util.stereotypes.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

//расписание работы докторов - указывается смена, в какую работает доктор в конкретную неделю
@Entity
@Table(name = "doctor_schedule", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"doctor_id", "startWeek"})})
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
    @NotNull
    @Immutable
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FirstDayOfWeek
    private LocalDate startWeek;

    public DoctorSchedule(Doctor doctor, WorkShift workShift, LocalDate startWeek) {
        this.doctor = doctor;
        this.workShift = workShift;
        this.startWeek = startWeek;
    }
}