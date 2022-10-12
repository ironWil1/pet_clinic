package com.vet24.service.pet.appearance;

import com.vet24.dao.pet.appearance.ColorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<String> getAllColors() {
        return colorDao.getAllColors();
    }

    @Override
    public Boolean isColorExists(String color) {
        return colorDao.isColorExists(color);
    }
}
