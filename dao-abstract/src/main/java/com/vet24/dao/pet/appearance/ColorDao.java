package com.vet24.dao.pet.appearance;

import java.util.List;

public interface ColorDao {
    List<String> findColor(String color);
    List<String> getAllColors();
    Boolean isColorExists(String color);

    void add(List<String> colors);

    void delete(List<String> colors);
}
