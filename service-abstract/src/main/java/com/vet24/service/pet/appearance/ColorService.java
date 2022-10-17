package com.vet24.service.pet.appearance;

import java.util.List;

public interface ColorService {
    List<String> findColor(String color);
    Boolean isColorExists(String color);
    void addColor(List<String> color);
    void deleteColor(List<String> color);
}
