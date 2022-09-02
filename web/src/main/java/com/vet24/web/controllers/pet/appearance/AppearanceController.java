package com.vet24.web.controllers.pet.appearance;

import com.vet24.service.pet.appearance.BreedService;
import com.vet24.service.pet.appearance.ColorService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appearance/")
public class AppearanceController {

    private final ColorService colorService;
    private final BreedService breedService;

    public AppearanceController(ColorService colorService, BreedService breedService) {
        this.colorService = colorService;
        this.breedService = breedService;
    }

    @Operation(summary = "Получение возможного окраса животного")
    @ApiResponse(responseCode = "200", description = "Окрас успешно получен или получен пустой список")
    @GetMapping("/color")
    public List<String> getColor(@RequestParam String text) {
        return colorService.findColor(text);
    }

    @Operation(summary = "Получение возможной породы животного")
    @ApiResponse(responseCode = "200", description = "Порода успешно получена или получен пустой список")
    @GetMapping("/breed")
    public List<String> getBreed(@RequestParam String petType, @RequestParam String text) {
        return breedService.getBreed(petType, text);
    }
}
