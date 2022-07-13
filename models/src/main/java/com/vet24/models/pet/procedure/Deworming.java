package com.vet24.models.pet.procedure;

import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "deworming")
public class Deworming {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "medicine_batch_number")
    private String medicineBatchNumber;

    @Column(name = "is_periodical")
    private Boolean isPeriodical;

    @Column(name = "period_days")
    private Integer periodDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public Deworming(LocalDate date,  String medicineBatchNumber,
                        Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        this.date = date;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
        this.medicine = medicine;
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Deworming deworming = (Deworming) o;
        return id != null && Objects.equals(id, deworming.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
