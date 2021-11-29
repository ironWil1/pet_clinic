package com.vet24.web.util;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReflectionUtil {

    @Value("${enums.location}")
    private String enumLoc;

    @Value("${enums.prefix.found}")
    private String enumFoundStr;

    public List<String> getAllEnums() {
        Reflections reflections = new Reflections(enumLoc);
        Set<Class<? extends Enum>> classez = reflections.getSubTypesOf(Enum.class);
        return classez.stream()
                .map(t -> t.getName().replaceFirst(enumFoundStr, ""))
                .collect(Collectors.toList());
    }



    public List<String> getEnumConsts(String enumName) {
        String found = enumFoundStr + enumName;
        List<String> result = new ArrayList<>();
        Reflections reflections = new Reflections(enumLoc);
        Set<Class<? extends Enum>> classez = reflections.getSubTypesOf(Enum.class);

        for (Class<? extends Enum> cl : classez) {
            if (found.contains(cl.getName())) {
                List<Enum> o = Arrays.asList(cl.getEnumConstants());
                for (Enum k : o) {
                    result.add(k.name());
                }
            }
        }
        return result;
    }

}
