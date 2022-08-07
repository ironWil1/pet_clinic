package com.vet24.models.medicine;

import com.vet24.models.track.ChangeTrackedEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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

    public Medicine(String manufactureName, String name, String iconUrl, String description) {
        this.manufactureName = manufactureName;
        this.name = name;
        this.iconUrl = iconUrl;
        this.description = description;
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