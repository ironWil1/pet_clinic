package com.vet24.models.user;

import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("CLIENT")
@EqualsAndHashCode(callSuper = true, exclude = "pets")
public class Client extends User {

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Pet> pets = new HashSet<>();

    public Client() {
        super();
    }

    public Client(String firstname, String lastname, String login, String password, Role role, Set<Pet> pets) {
        super(firstname, lastname, login, password, role);
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setClient(this);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setClient(null);
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "Client{" +
                "pets=" + pets + " login " + super.getLogin() + " " + super.getRole() +
                '}';
    }
}
