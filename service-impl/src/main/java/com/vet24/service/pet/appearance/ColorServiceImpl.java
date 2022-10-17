package com.vet24.service.pet.appearance;

import com.vet24.dao.pet.appearance.ColorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {

    private final ColorDao colorDao;

    @Autowired
    public ColorServiceImpl(ColorDao colorDao) {
        this.colorDao = colorDao;
    }

    @Override
    public List<String> findColor(String color) {
        return colorDao.findColor(color);
    }

    @Override
    public Boolean isColorExists(String color) {
        return colorDao.isColorExists(color);
    }

    @Override
    public void addColor(List<String> color) {
        colorDao.addColor(color.stream()
                .filter(this::isColorExists)
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteColor(List<String> color) {

    }

}
