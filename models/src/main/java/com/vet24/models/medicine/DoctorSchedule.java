package com.vet24.models.medicine;

import com.vet24.models.enums.WorkShift;
import com.vet24.models.user.User;
import com.vet24.models.validation.FirstDayOfWeek;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.internal.util.stereotypes.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
    private User doctor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkShift workShift;

    @Column
    @NotNull
    @Immutable
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FirstDayOfWeek
    private LocalDate startWeek;

    public DoctorSchedule(User doctor, WorkShift workShift, LocalDate startWeek) {
        this.doctor = doctor;
        this.workShift = workShift;
        this.startWeek = startWeek;
    }
}