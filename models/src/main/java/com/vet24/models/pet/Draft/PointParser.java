package com.vet24.models.pet.Draft;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class PointParser implements GeometryParser<Point> {

    private GeometryFactory geometryFactory;

    public static Coordinate coordinateFromJson(JsonNode array) {
        assert array.isArray() && (array.size() == 2 || array.size() == 3) : "expecting coordinate array with single point [ x, y, |z| ]";

        if (array.size() == 2) {
            return new Coordinate(
                    array.get(0).asDouble(),
                    array.get(1).asDouble());
        }

        return new Coordinate(
                array.get(0).asDouble(),
                array.get(1).asDouble(),
                array.get(2).asDouble());
    }

    public static Coordinate[] coordinatesFromJson(JsonNode array) {
        Coordinate[] points = new Coordinate[array.size()];
        for (int i = 0; i != array.size(); ++i) {
            points[i] = PointParser.coordinateFromJson(array.get(i));
        }
        return points;
    }

    public Point pointFromJson(JsonNode node) {
        return geometryFactory.createPoint(
                coordinateFromJson(node.get("coordinates")));
    }

    @Override
    public Point geometryFromJson(JsonNode node) throws JsonMappingException {
        return pointFromJson(node);
    }
}

