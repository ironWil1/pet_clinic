package com.vet24.dao.pet.appearance;

import java.util.List;

public interface BreedDao {
    List<String> getBreedByPetTypeByBreed(String petType, String breed);

    List<String> getBreedByBreed(String breed);

    Boolean isPetTypeAndBreedCombinationExist(String petType, String breed);

    void addBreeds(String petType, List<String> breeds);

    void deleteBreeds(String petType, List<String> breeds);
}
