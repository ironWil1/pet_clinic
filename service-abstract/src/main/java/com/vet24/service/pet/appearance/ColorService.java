package com.vet24.service.pet.appearance;

import java.util.List;

public interface ColorService {
    List<String> findColor(String color);
    List<String> findAll();
    Boolean isColorExists(String color);
}
