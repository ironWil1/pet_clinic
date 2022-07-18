package com.vet24.web.controllers.pet;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.PetContactMapper;
import com.vet24.models.pet.Pet;
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
import org.webjars.NotFoundException;

import javax.validation.Valid;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

@RestController
@Slf4j
@RequestMapping("api/client/pet/contact")
@Tag(name = "petContact-controller", description = "operations with Pets' Contacts")
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

    @Operation(summary = "Get Pet Contact by pet ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got a Pet Contact",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetContactDto.class))),
            @ApiResponse(responseCode = "403", description = "Pet Contact was not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet Contact is not yours",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/")
    public ResponseEntity<PetContactDto> getById(@RequestParam Long petId) {
        Client client = (Client)  getSecurityUserOrNull();
        Pet pet = petService.getByKey(petId);
        if (pet == null) {
            throw new NotFoundException("The pet was not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("This pet is not yours");
        }
        PetContact petContact = petContactService.getByPetId(petId);
        return new ResponseEntity<>(petContactMapper.toDto(petContact), HttpStatus.OK);
    }

    @Operation(summary = "Update Pet Contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Pet Contact"),
            @ApiResponse(responseCode = "404", description = "Pet or Client is not found"),
            @ApiResponse(responseCode = "400", description = "Pet owner ID and current Client ID do not match")
    })
    @PutMapping("/")
    public ResponseEntity<PetContactDto> updatePetContact(@Valid @RequestBody PetContactDto petContactDto, @RequestParam Long petId) {

        Client client = (Client) getSecurityUserOrNull();
        Pet pet = petService.getByKey(petId);
        if (client != null && pet != null){
            if (!(pet.getClient().getId().equals(client.getId()))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            PetContact petContact = pet.getPetContact();
            petContactMapper.updateEntity(petContactDto, petContact);
            petContact.setCode(petContact.getCode());
            petContactService.update(petContact);
            return ResponseEntity.status(201).body(petContactDto);
        }
        return ResponseEntity.notFound().build();
    }

}
