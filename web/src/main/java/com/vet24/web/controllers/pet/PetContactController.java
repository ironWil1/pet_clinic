package com.vet24.web.controllers.pet;

import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.models.mappers.pet.PetContactMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.models.user.Client;
import com.vet24.service.pet.PetContactService;

import com.vet24.service.pet.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

@RestController
@Slf4j
@RequestMapping("api/client/pet/contact")
@Tag(name = "petContact-controller", description = "Операции над Контактами питомцев")
public class PetContactController {

    private final PetContactService petContactService;
    private final PetContactMapper petContactMapper;
    private final PetService petService;

    @Autowired
    public PetContactController(PetContactService petContactService, PetContactMapper petContactMapper, PetService petService) {
        this.petContactService = petContactService;
        this.petContactMapper = petContactMapper;
        this.petService = petService;
    }

    @Operation(summary = "Получние Контакта питомца по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт питомца получен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetContactDto.class))),
            @ApiResponse(responseCode = "400", description = "Питомец не найден или Вам не принадлежит")
    })
    @GetMapping("/")
    public ResponseEntity<PetContactDto> getPetContactByPetId(@RequestParam Long petId) {
        Client client = (Client) getSecurityUserOrNull();
        if (petService.isExistByPetIdAndClientId(petId, client.getId())) {
            PetContact petContact = petContactService.getByKey(petId);
            return new ResponseEntity<>(petContactMapper.toDto(petContact), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Обновление Контакта питомца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт питомца обновлен"),
            @ApiResponse(responseCode = "400", description = "Питомец не найден или Вам не принадлежит")
    })
    @PutMapping("/")
    public ResponseEntity<PetContactDto> updatePetContact(@Valid @RequestBody PetContactDto petContactDto, @RequestParam Long petId) {

        Client client = (Client) getSecurityUserOrNull();
        if (petService.isExistByPetIdAndClientId(petId, client.getId())) {
            PetContact petContact = petContactService.getByKey(petId);
            petContactMapper.updateEntity(petContactDto, petContact);
            petContactService.update(petContact);
            return new ResponseEntity<>(petContactDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
