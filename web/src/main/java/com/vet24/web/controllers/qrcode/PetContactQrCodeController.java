package com.vet24.web.controllers.qrcode;

import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.models.mappers.PetContactMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import com.vet24.util.qrcode.PetContactQrCodeGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @Operation(summary = "Encode and create qr code for pet contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully create the qr code", content = @Content()),
            @ApiResponse(responseCode = "404", description = "PetContact ID is not found"),
    })
    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> createPetContactQrCode(@PathVariable("id") Long id) {
        if (petContactService.isExistByKey(id)) {
            PetContact petContact = petContactService.getByKey(id);
            String UrlToAlertPetContact = "/api/petFound?petCode=" + petContact.getPetCode();
            String sb = "Имя питомца - " + petContact.getPet().getName() + ", " +
                    "Владелец - " + petContact.getOwnerName() + ", " +
                    "Адрес - " + petContact.getAddress() + ", " +
                    "Телефон - " + petContact.getPhone() + ". " +
                    "Чтобы сообщить владельцу о находке перейдите по адресу - " + UrlToAlertPetContact;
            return ResponseEntity.ok(PetContactQrCodeGenerator.generatePetContactQrCodeImage(sb));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create and update PetContact for qr code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully update the PetContact",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "201", description = "Successfully create the PetContact",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "PetContact is expecting a pet for persist command"),
    })
    @PostMapping(value = "/{id}/qr")
    public ResponseEntity<PetContactDto> saveOrUpdatePetContact(@RequestBody PetContactDto petContactDto,
                                                                @PathVariable("id") Long id) {
        if (petContactService.isExistByKey(id)) {
            PetContact petContactNew = petContactMapper.petContactDtoToPetContact(petContactDto);
            PetContact petContactOld = petContactService.getByKey(id);
            petContactOld.setOwnerName(petContactNew.getOwnerName());
            petContactOld.setAddress(petContactNew.getAddress());
            petContactOld.setPhone(petContactNew.getPhone());
            petContactService.update(petContactOld);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else if (!petContactService.isExistByKey(id) && petService.isExistByKey(id)) {
            Pet pet = petService.getByKey(id);
            PetContact petContact = petContactMapper.petContactDtoToPetContact(petContactDto);
            petContact.setPetCode(petContactService.randomPetContactUniqueCode(id));
            petContact.setPet(pet);
            petContactService.persist(petContact);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}