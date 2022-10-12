package com.vet24.web.controllers.pet.appearance;

import com.vet24.service.pet.appearance.BreedService;
import com.vet24.service.pet.appearance.ColorService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public List<String> getColor(@RequestParam(required = false) Optional<String> text) {
        if (text.isEmpty() || Strings.isBlank(text.get()))
            return colorService.getAllColors();
        else
            return colorService.findColor(text.get());
    }

    @Operation(summary = "Получение возможной породы животного")
    @ApiResponse(responseCode = "200", description = "Порода успешно получена или получен пустой список")
    @GetMapping("/breed")
    public List<String> getBreed(@RequestParam(required = false) Optional<String> petType,
                                 @RequestParam(required = false) Optional<String> text) {
        if (text.isPresent() && Strings.isNotBlank(text.get())) {
            return petType.isPresent() ?
                    breedService.getBreed(petType.get(), text.get()) : breedService.getBreed("", text.get());
        } else if (petType.isEmpty() || Strings.isBlank(petType.get())) {
            return breedService.getAllBreeds();
        } else {
            return breedService.getBreedsByPetType(petType.get());
        }
    }
}
