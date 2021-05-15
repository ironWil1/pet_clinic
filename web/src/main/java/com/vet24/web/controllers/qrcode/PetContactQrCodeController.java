package com.vet24.web.controllers.qrcode;

import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.models.mappers.PetContactMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.security.exceptions.petContact.NoSuchPetContactIdException;
import com.vet24.security.exceptions.petContact.NoSuchPetForPetContactException;
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
            throws NoSuchPetContactIdException, NumberFormatException {
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
            throw new NoSuchPetContactIdException("Пользователь с ID = " + id + " не найден");
        }
    }

    @PostMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<PetContactDto> saveOrUpdatePetContact(@RequestBody PetContactDto petContactDto,
                                                                @PathVariable("id") Long id)
            throws NoSuchPetForPetContactException, NumberFormatException {
        if (petContactService.isExistByKey(id)) {
            PetContact petContactNew = petContactMapper.petContactDtoToPetContact(petContactDto);
            PetContact petContactOld = petContactService.getByKey(id);
            petContactOld.setOwnerName(petContactNew.getOwnerName());
            petContactOld.setAddress(petContactNew.getAddress());
            petContactOld.setPhone(petContactNew.getPhone());
            petContactService.update(petContactOld);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            // Создаёт PetContact только при условии что Pet уже создан либо операции выполняются транзакционально.
            // Без Pet падает с ошибкой "attempted to assign id from null one-to-one property" из-за @OneToOne с @MapsId.
            try {
                Pet pet = petService.getByKey(id);
                PetContact petContact = petContactMapper.petContactDtoToPetContact(petContactDto);
                petContact.setPetCode(petContactService.randomPetContactUniqueCode(id));
                petContact.setPet(pet);
                petContactService.persist(petContact);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (RuntimeException e) {
                throw new NoSuchPetForPetContactException("Предварительно заведите карточку питомца для " + petContactDto.getOwnerName());
            }
        }
    }
}