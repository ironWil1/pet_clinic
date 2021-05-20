package com.vet24.models.pet.Draft;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Geometry;

public interface GeometryParser <T extends Geometry> {

    T geometryFromJson(JsonNode node) throws JsonMappingException;
}
