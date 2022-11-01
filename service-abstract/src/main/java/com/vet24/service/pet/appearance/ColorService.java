package com.vet24.service.pet.appearance;

import java.util.List;

public interface ColorService {
    List<String> findColor(String color);
    List<String> getAllColors();
    Boolean isColorExists(String color);
    void add(List<String> colors);
    void delete(List<String> colors);
}
