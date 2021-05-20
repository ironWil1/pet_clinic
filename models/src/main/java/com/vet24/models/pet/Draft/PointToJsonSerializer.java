package com.vet24.models.pet.Draft;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Point;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class PointToJsonSerializer extends JsonSerializer<Point> {

    @Override
    public void serialize(Point value, JsonGenerator jsonGenerator,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("x", value.getY());
        jsonGenerator.writeNumberField("y", value.getX());
        jsonGenerator.writeEndObject();

        /*String jsonValue = "null";
        try
        {
            if(value != null) {
                double lat = value.getY();
                double lon = value.getX();
                jsonValue = String.format("POINT (%s %s)", lat, lon);
            }
        }
        catch(Exception e) {}

        jgen.writeString(jsonValue);*/
    }
}
