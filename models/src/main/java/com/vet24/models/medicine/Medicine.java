package com.vet24.models.medicine;

import com.vet24.models.track.ChangeTrackedEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"manufacture_name" , "name"})},
        name = "medicine")
public class Medicine extends ChangeTrackedEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "manufacture_name")
    private String manufactureName;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "icon_url")
    private String iconUrl;

    @Column(nullable = false, name = "description")
    private String description;

    @OneToMany(
            mappedBy = "medicine",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Dosage> dosages = new ArrayList<>();

    public Medicine(String manufactureName, String name, String iconUrl, String description) {
        this.manufactureName = manufactureName;
        this.name = name;
        this.iconUrl = iconUrl;
        this.description = description;
    }

    public void addDosage(Dosage dosage) {
        dosages.add(dosage);
        dosage.setMedicine(this);
    }

    public void removeDosage(Dosage dosage) {
        dosages.remove(dosage);
        dosage.setMedicine(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Medicine medicine = (Medicine) o;
        return id != null && Objects.equals(id, medicine.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}