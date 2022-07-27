package com.vet24.web.controllers.qrcode;

import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.PetContactMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/client/pet")
public class PetContactQrCodeController {
    private final PetService petService;
    private final PetContactService petContactService;
    private final PetContactMapper petContactMapper;

    public PetContactQrCodeController(PetService petService, PetContactService petContactService, PetContactMapper petContactMapper) {
        this.petService = petService;
        this.petContactService = petContactService;
        this.petContactMapper = petContactMapper;
    }

    @Operation(summary = "Create and update PetContact for qr code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update the PetContact",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "201", description = "Successfully create the PetContact",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "PetContact is expecting a pet for persist command"),
            @ApiResponse(responseCode = "400", description = "PetContact is expecting a pet for persist command"),
    })
    @PostMapping(value = "/{id}/qr")
    public ResponseEntity<PetContactDto> saveOrUpdatePetContact( @PathVariable("id") Long id,
                                                                 @Valid @RequestBody PetContactDto petContactDto) {

        if (petContactService.isExistByKey(id)) {
            PetContact petContactOld = petContactService.getByKey(id);
            if (petContactDto.getOwnerName() == null || petContactDto.getOwnerName().equals("")) {
                petContactDto.setOwnerName(petContactOld.getOwnerName());
            }
            if (petContactDto.getAddress() == null || petContactDto.getAddress().equals("")) {
                petContactDto.setAddress(petContactOld.getAddress());
            }
            if (petContactDto.getPhone() == null || petContactDto.getPhone() == 0) {
                petContactDto.setPhone(petContactOld.getPhone());
            }
            PetContact petContactNew = petContactMapper.toEntity(petContactDto);
            petContactOld.setOwnerName(petContactNew.getOwnerName());
            petContactOld.setAddress(petContactNew.getAddress());
            petContactOld.setPhone(petContactNew.getPhone());
            petContactService.update(petContactOld);
            log.info("The pet contact for pet with id {} was updated",id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (petService.isExistByKey(id)) {
            if (petContactDto.getOwnerName() == null || petContactDto.getOwnerName().equals("")) {
                throw new BadRequestException("name can't is null or is empty for create Contact");
            }
            if (petContactDto.getAddress() == null || petContactDto.getAddress().equals("")) {
                throw new BadRequestException("Address can't is null or is empty for create Contact");
            }
            if (petContactDto.getPhone() == null || petContactDto.getPhone() == 0) {
                throw new BadRequestException("phone can't is null or empty for create Contact");
            }
            Pet pet = petService.getByKey(id);
            PetContact petContact = petContactMapper.toEntity(petContactDto);
            petContact.setCode(petContactService.randomPetContactUniqueCode());
            petContact.setPet(pet);
            petContactService.persist(petContact);
            log.info("The pet contact for pet with id {} was saved",id);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
