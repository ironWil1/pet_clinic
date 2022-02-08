package com.vet24.models.medicine;

import com.vet24.models.user.Doctor;
import lombok.*;
import com.vet24.models.enums.WorkShift;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

//расписание работы докторов - указывается смена, в какю работает доктор в конкретную неделю
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
    private LocalDate startWeek;
    //private Integer weekNumber;

    public DoctorSchedule(Doctor doctor, WorkShift workShift, LocalDate startWeek) {
        this.doctor = doctor;
        this.workShift = workShift;
        this.startWeek = startWeek;
    }
}
/*
Заменить в DoctorSchedule поле  int weekNumber на поле LocalDate startWeek, при чем:
startWeek - всегда понедельник
поле неизменяемое
Не null

Необходимо:
поправить сущность
добавить валидацию при сохранении или изменении сущности.
Поправить контроллеры и тесты.
 */