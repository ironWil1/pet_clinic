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
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class Client extends User {

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Pet> pets =  new ArrayList<>();

    @OneToMany(
                    mappedBy = "client",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true
            )
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL
    )
    private List<CommentReaction> commentReactions = new ArrayList<>();

    public Client() {
        super();
    }

    public Client(String firstname, String lastname, String email, String password, Role role, List<Pet> pets) {
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

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<CommentReaction> getCommentReactions() {
        return commentReactions;
    }

    public void setCommentReactions(List<CommentReaction> commentReactions) {
        this.commentReactions = commentReactions;
    }

    @Override
    public String toString() {
        return "Client{" +
                "pets=" + pets + " email " + super.getEmail() + " " + super.getRole() +
                '}';
    }
}
