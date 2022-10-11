package com.vet24.dao.pet.appearance;

import java.util.List;

public interface ColorDao {
    List<String> findColor(String color);
    List<String> findAll();
    Boolean isColorExists(String color);
}
