package com.vet24.dao.pet.appearance;

import java.util.List;

public interface BreedDao {
    List<String> getBreedByPetTypeByBreed(String petType, String breed);

    List<String> getBreedByBreed(String breed);

    List<String> getAllBreeds();

    List<String> getBreedsByPetType(String petType);

    Boolean isPetTypeAndBreedCombinationExist(String petType, String breed);
}
