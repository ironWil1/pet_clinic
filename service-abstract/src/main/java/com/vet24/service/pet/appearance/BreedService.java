package com.vet24.service.pet.appearance;

import java.util.List;

public interface BreedService {
    List<String> getBreed(String petType, String breed);
    List<String> getBreedByBreed(String breed);
    List<String> getBreedsByPetType(String petType);
    List<String> getAllBreeds();
    Boolean isBreedExists(String petType, String breed);

    void addBreeds(String petType, List<String> breeds);

    void deleteBreeds(String petType, List<String> breeds);
}
