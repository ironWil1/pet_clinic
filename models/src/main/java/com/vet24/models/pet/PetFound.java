package com.vet24.models.pet;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vet24.models.pet.Draft.JsonToPointDeserializer;
import com.vet24.models.pet.Draft.PointToJsonSerializer;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class PetFound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = PointToJsonSerializer.class)
    @JsonDeserialize(using = JsonToPointDeserializer.class)
    @Column(columnDefinition = "Point")
    private Point location; // посмотреть в каком формате лучше всего данные о местонахождении передавать

    private String text; // текст сообщения, в котором будет написан текст нашедшим питомца
}

