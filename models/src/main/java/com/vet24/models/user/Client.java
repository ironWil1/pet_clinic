package com.vet24.models.user;

import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("CLIENT")
@EqualsAndHashCode(callSuper = true, exclude = {"pets","comments","likes"})
@Getter
public class Client extends User {

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Pet> pets = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "id")
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL
    )
    private Set<Like> likes = new HashSet<>();

    public Client() {
        super();
    }

    public Client(String firstname, String lastname, String email, String password, Role role, Set<Pet> pets) {
        super(firstname, lastname, email, password, role);
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
                "pets=" + pets + " email " + super.getEmail() + " " + super.getRole() +
                '}';
    }
}
