package com.vet24.web.controllers.user;

import com.vet24.models.enums.PetType;
import com.vet24.service.pet.appearance.BreedService;
import com.vet24.web.controllers.pet.appearance.AppearanceController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.NotBlank;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/manager/appearance")
@Tag(name = "manager appearance controller", description = "operations with appearance")
public class AppearanceManagerController {

    private final BreedService breedService;
    private final AppearanceController appearanceController;

    public AppearanceManagerController(BreedService breedService, AppearanceController appearanceController) {
        this.breedService = breedService;
        this.appearanceController = appearanceController;
    }

    @GetMapping("/breed")
    @Operation(summary = "get breed(s)")
    @ApiResponse(responseCode = "200", description = "Порода(ы) получена")
    public ResponseEntity<List<String>> getBreed(@RequestParam(required = false) String petType, @RequestParam(required = false) String breed) {
        return new ResponseEntity<>(appearanceController.getBreed(petType, breed), HttpStatus.OK);
    }


    @Operation(summary = "add new breed(s)")
    @ApiResponse(responseCode = "200", description = "Порода(ы) добавлена в базу данных")
    @PostMapping("/breed")
    public ResponseEntity<Void> addBreeds(@RequestParam @NotBlank PetType petType,
                                     @RequestBody List<String> breeds) {
        breedService.addBreeds(petType.toString().strip(), breeds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "delete breed(s)")
    @ApiResponse(responseCode = "200", description = "Порода(ы) удалена из базы данных")
    @DeleteMapping("/breed")
    public ResponseEntity<Void> deleteBreeds(@RequestParam @NotBlank PetType petType,
                                       @RequestBody List<String> breeds) {
        breedService.deleteBreeds(petType.toString().strip(), breeds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
