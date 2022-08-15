package com.vet24.web.controllers.found;

import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.mappers.pet.PetContactMapper;
import com.vet24.models.mappers.pet.PetFoundMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.PetFound;
import com.vet24.service.media.MailService;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetFoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;

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

    private final PetContactMapper petContactMapper;


    public PetFoundController(PetFoundService petFoundService,
                              PetContactService petContactService,
                              PetFoundMapper petFoundMapper,
                              MailService mailService,
                              PetContactMapper petContactMapper) {
        this.petFoundService = petFoundService;
        this.petContactService = petContactService;
        this.petFoundMapper = petFoundMapper;
        this.mailService = mailService;
        this.petContactMapper = petContactMapper;
    }

    /* Запрос может выглядеть следующим образом.
    {
        "latitude" : "1.2345678",
        "longitude" : "2.3456789",
        "text" : "Какой-то текст"
    }*/

    @Operation(summary = "Сохранение данных найденного питомца и создание с отправкой владельцу сообщения о питомце")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Данные найденного питомца успешно сохранены, сообщение создано",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Данные о питомце по коду не найдены"),
    })
    @PostMapping(value = "")
    public ResponseEntity<PetFoundDto> savePetFoundAndSendOwnerPetMessage(@RequestParam(value = "code", required = false) String code,
                                                                          @RequestBody PetFoundDto petFoundDto) {
        if (petContactService.isExistByCode(code)) {
            PetContact petContact = petContactService.getByCode(code);
            PetFound petFound = petFoundMapper.toEntity(petFoundDto);
            petFound.setPet(petContact.getPet());
            petFound.setFoundDate(LocalDateTime.now());

            petFoundService.persist(petFound);

            String message = petFound.getMessage();
            String geolocationPetFoundUrl = String.format(googleMapsServiceUrl, petFound.getLatitude(), petFound.getLongitude());
            log.info("Pet with this code {} found on the latitude{} and longitude {}", code, petFound.getLatitude(), petFound.getLongitude());
            mailService.sendGeolocationPetFoundMessage(petContact, geolocationPetFoundUrl, message);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            log.info("Pet with this code {} does not exist or was not found", code);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "получение контактной информации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контактная информация успешно получена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetContactDto.class))),
            @ApiResponse(responseCode = "404", description = "Контактная информация не найдена")
    })
    @GetMapping(value = "")
    public ResponseEntity<PetContactDto> getPetContaсtInfo(@RequestParam(value = "code") String code) {
        PetContact petContact = petContactService.getByCode(code);
        if (petContactService.isExistByCode(code)) {
            return new ResponseEntity<>(petContactMapper.toDto(petContact), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
