package com.vet24.models.user;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.pet.Pet;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_pets", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    private Set<Pet> pets;

    public Client() {
        super();
    }

    public Client(String firstname, String lastname, String login, String password, Role role, Set<Pet> pets) {
        super(firstname, lastname, login, password, role);
        this.pets = pets;
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
