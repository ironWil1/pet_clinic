package com.vet24.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;


public class Client extends User {

    private Set<Pet> pets;
}
