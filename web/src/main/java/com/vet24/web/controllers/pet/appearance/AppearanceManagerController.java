package com.vet24.web.controllers.pet.appearance;

import com.vet24.service.pet.appearance.ColorService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manager/appearance/color")
public class AppearanceManagerController {

    private final ColorService colorService;

    public AppearanceManagerController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public List<String> getColor(@RequestParam(required = false) String text) {
        return text != null ? colorService.findColor(text) : new ArrayList<>();
    }

    @PostMapping
    public void saveColor(@RequestBody List<String> color) {
        color.stream()
                .filter(colorToSave -> !colorService.isColorExists(colorToSave))
                .collect(Collectors.toSet());
    }
}
