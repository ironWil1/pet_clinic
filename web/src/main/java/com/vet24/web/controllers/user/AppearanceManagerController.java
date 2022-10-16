package com.vet24.web.controllers.user;

import com.vet24.service.pet.appearance.BreedService;
import com.vet24.web.controllers.pet.appearance.AppearanceController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
    @ApiResponse(responseCode = "200", description = "Breed(s) retrieved")
    public ResponseEntity<List<String>> getBreed(@RequestParam(required = false) String petType, @RequestParam(required = false) String breed) {
        return new ResponseEntity<>(appearanceController.getBreed(petType, breed), HttpStatus.OK);
    }


    @Operation(summary = "add new breed(s)")
    @ApiResponse(responseCode = "200", description = "Breed(s) added to DB")
    @ApiResponse(responseCode = "400", description = "Bad request (petType should not be empty)")
    @PostMapping("/breed")
    public ResponseEntity<Void> save(@RequestParam String petType,
                                     @RequestBody List<String> breeds) {
        if (petType.isBlank())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        breedService.addBreeds(petType.strip(), breeds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "delete breed(s)")
    @ApiResponse(responseCode = "200", description = "Breed(s) deleted from DB")
    @ApiResponse(responseCode = "400", description = "Bad request (petType should not be empty)")
    @DeleteMapping("/breed")
    public ResponseEntity<Void> delete(@RequestParam String petType,
                                       @RequestBody List<String> breeds) {
        if (petType.isBlank())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        breedService.deleteBreeds(petType.strip(), breeds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
