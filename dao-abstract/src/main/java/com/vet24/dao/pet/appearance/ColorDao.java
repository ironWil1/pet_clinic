package com.vet24.dao.pet.appearance;

import java.util.List;

public interface ColorDao {
    List<String> findColor(String color);

    Boolean isColorExists(String color);

    void addColor(List<String> collect);
}
