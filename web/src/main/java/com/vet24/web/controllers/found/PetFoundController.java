package com.vet24.web.controllers.found;

import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.mappers.pet.PetFoundMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.PetFound;
import com.vet24.service.media.MailService;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetFoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/petFound")
public class PetFoundController {

    @Value("${googlemaps.service.url}")
    private String googleMapsServiceUrl;

    private final PetFoundService petFoundService;
    private final PetContactService petContactService;
    private final PetFoundMapper petFoundMapper;
    private final MailService mailService;

    public PetFoundController(PetFoundService petFoundService, PetContactService petContactService,
                              PetFoundMapper petFoundMapper, MailService mailService) {
        this.petFoundService = petFoundService;
        this.petContactService = petContactService;
        this.petFoundMapper = petFoundMapper;
        this.mailService = mailService;
    }

    /* Запрос может выглядеть следующим образом.
    {
        "latitude" : "1.2345678",
        "longitude" : "2.3456789",
        "text" : "Какой-то текст"
    }*/
    @Operation(summary = "Save data found pet and create with send owner message about pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully save data found and create message",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "PetContact by petCode is not found"),
    })
    @PostMapping(value = "")
    public ResponseEntity<PetFoundDto> savePetFoundAndSendOwnerPetMessage(@RequestParam(value = "petCode", required = false) String petCode,
                                                                          @RequestBody PetFoundDto petFoundDto) {
        if (petContactService.isExistByPetCode(petCode)) {
            PetContact petContact = petContactService.getByPetCode(petCode);
            PetFound petFound = petFoundMapper.toEntity(petFoundDto);
            petFound.setPet(petContact.getPet());
            petFoundService.persist(petFound);

            String text = petFound.getMessage();
            String geolocationPetFoundUrl = String.format(googleMapsServiceUrl, petFound.getLatitude(), petFound.getLongitude());
            log.info("Pet with this petCode {} found on the latitude{} and longitude {}", petCode,petFound.getLatitude(),petFound.getLongitude());
            mailService.sendGeolocationPetFoundMessage(petContact, geolocationPetFoundUrl, text);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            log.info("Pet with this petCode {} does not exist or was not found",petCode);
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
    }
}
