package com.vet24.models.dto.pet;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

@Data
public class PetFoundDto {

    private Point location; // посмотреть в каком формате лучше всего данные о местонахождении передавать
    private String text; // текст сообщения, в котором будет написан текст нашедшим питомца
}
