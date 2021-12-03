package com.vet24.web.util;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReflectionUtil {

    @Value("${enums.location}")
    private String enumLoc;

    public List<String> getAllEnums() {
        Reflections reflections = new Reflections(enumLoc);
        Set<Class<? extends Enum>> classez = reflections.getSubTypesOf(Enum.class);
        return classez.stream()
                .map(t -> t.getName().replaceFirst(enumLoc + ".", ""))
                .collect(Collectors.toList());
    }

    public List<String> getEnumConsts(String enumName) throws ClassNotFoundException {
        Class<? extends Enum> found = (Class<? extends Enum>) Class.forName(enumLoc + "." + enumName);
        return Arrays.stream(found.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
