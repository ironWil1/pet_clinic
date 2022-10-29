package com.vet24.web.controllers.user;

import com.vet24.models.enums.PetType;
import com.vet24.service.pet.appearance.BreedService;
import com.vet24.service.pet.appearance.ColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/api/manager/appearance")
@Tag(name = "manager appearance controller", description = "operations with appearance")
public class AppearanceManagerController {

    private final BreedService breedService;
    private final ColorService colorService;

    public AppearanceManagerController(BreedService breedService, ColorService colorService) {
        this.breedService = breedService;
        this.colorService = colorService;
    }

    @GetMapping("/breed")
    @Operation(summary = "Получить породу(ы)")
    @ApiResponse(responseCode = "200", description = "Порода(ы) получена")
    public ResponseEntity<List<String>> getBreed(@RequestParam(required = false) PetType petType, @RequestParam(required = false) String breed) {
        List<String> response;
        if (breed == null || breed.isBlank()) {
            response = (petType == null || petType.toString().isBlank()) ?
                    breedService.getAllBreeds() : breedService.getBreedsByPetType(petType.toString());
        } else {
            response = (petType == null) ?
                    breedService.getBreedByBreed(breed) : breedService.getBreed(petType.toString(), breed);

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Добавить породу(ы)")
    @ApiResponse(responseCode = "200", description = "Порода(ы) добавлена в базу данных")
    @PostMapping("/breed")
    public ResponseEntity<Void> addBreeds(@RequestParam PetType petType,
                                     @RequestBody List<String> breeds) {
        breedService.addBreeds(petType.toString(), breeds.stream()
                .map(s -> s.toLowerCase().trim()).collect(Collectors.toList()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить породу(ы)")
    @ApiResponse(responseCode = "200", description = "Порода(ы) удалена из базы данных")
    @DeleteMapping("/breed")
    public ResponseEntity<Void> deleteBreeds(@RequestParam PetType petType,
                                       @RequestBody List<String> breeds) {
        breedService.deleteBreeds(petType.toString(), breeds.stream()
                .map(s -> s.toLowerCase().trim()).collect(Collectors.toList()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/color")
    @Operation(summary = "Получить цвет(а)")
    @ApiResponse(responseCode = "200", description = "Цвет(а) получен(ы)")
    public ResponseEntity<List<String>> getColor(@RequestParam(required = false) String color) {
        List<String> colorList = color == null || color.isBlank() ? colorService.getAllColors() :
                colorService.findColor(color.trim().toLowerCase());
        return new ResponseEntity<>(colorList, HttpStatus.OK);
    }

    @Operation(summary = "Добавить цвет(а)")
    @ApiResponse(responseCode = "200", description = "Цвет(а) добавлен(ы) в базу данных")
    @PostMapping("/color")
    public ResponseEntity<Void> addColors(@RequestBody List<String> colors) {
        colorService.add(colors.stream()
                .map(s -> s.toLowerCase().trim())
                .collect(Collectors.toList()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить цвет(а)")
    @ApiResponse(responseCode = "200", description = "Цвет(а) удален(ы) из базы данных")
    @DeleteMapping("/color")
    public ResponseEntity<Void> deleteColors(@RequestBody List<String> colors) {
        colorService.delete(colors.stream()
                .map(s -> s.toLowerCase().trim()).collect(Collectors.toList()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
