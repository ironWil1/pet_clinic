package com.vet24.models.pet;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "pet_contact")
public class PetContact {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "owner_name")
    private String ownerName;

    @NonNull
    @Column(name = "address")
    private String address;

    @NonNull
    @Column(name = "phone")
    private Long phone;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(unique = true, updatable = false, name = "code")
    private String code;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Pet pet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PetContact that = (PetContact) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}