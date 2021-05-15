package com.vet24.web.controllers.qrcode;

import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.models.mappers.PetContactMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.security.exceptions.NoSuchPetContactException;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import com.vet24.util.qrcode.PetContactQrCodeGenerator;
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

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> createPetContactQrCode(@PathVariable("id") Long id)
            throws NoSuchPetContactException, NumberFormatException {


        try {
            PetContact petContact = petContactService.getByKey(id);
            String UrlToAlertPetContact = "/api/petFound?petCode=" + petContact.getPetCode();
            String sb = "Имя питомца - " + petContact.getPet().getPetName() + ", " +
                    "Владелец - " + petContact.getOwnerName() + ", " +
                    "Адрес - " + petContact.getAddress() + ", " +
                    "Телефон - " + petContact.getPhone() + ". " +
                    "Чтобы сообщить владельцу о находке перейдите по адресу - " + UrlToAlertPetContact;
            return ResponseEntity.ok(PetContactQrCodeGenerator.generatePetContactQrCodeImage(sb));
        } catch (RuntimeException e) {
            throw new NoSuchPetContactException("Пользователь с ID = " + id + " не найден");
        }
    }

    @PostMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<PetContactDto> savePetContact(@RequestBody PetContactDto petContactDto,
                                                        @PathVariable("id") Long id)
            throws NoSuchPetContactException, NumberFormatException {
        PetContact petContact = petContactMapper.petContactDtoToPetContact(petContactDto);
        petContact.setPetCode(petContactService.randomPetContactUniqueCode(id));
        petContactService.persist(petContact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}