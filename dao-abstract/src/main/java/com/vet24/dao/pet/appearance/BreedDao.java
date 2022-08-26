package com.vet24.dao.pet.appearance;

import java.util.List;

public interface BreedDao {
    List<String> getBreed(String petType,String breed);
}
