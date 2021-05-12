package com.vet24.web.controllers.qr_code;

import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.models.mappers.PetContactMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.models.qrcode.QRCodeGenerator;
import com.vet24.service.pet.PetContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/pet")
public class QRCodeController {

    @Autowired
    private PetContactService petContactService;

    @Autowired
    private PetContactMapper petContactMapper;

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> createZxingQRCode(@PathVariable("id") Long id) throws Exception {
        final String URL = "/api/petFound?petCode={petCode}";
        PetContact pet = petContactService.getByKey(id);
        String barcode = "";
        barcode += "Имя питомца - " + pet.getPet().getPetName() + ", ";
        barcode += "Владелец - " + pet.getOwnerName() + ", ";
        barcode += "Адрес - " + pet.getAddress() + ", ";
        barcode += "Телефон - " + pet.getPhone() + ". ";
        barcode += "Чтобы сообщить владельцу о находке перейдите по адресу - " + URL;

        //URL = /api/petFound?petCode={petCode} - ссылка, на которую надо
        //будет перейти для оповещения владельца. petCode берется из базы.

        System.out.println(barcode);
        return ResponseEntity.ok(QRCodeGenerator.generateQRCodeImage(barcode));
    }

    // GET /api/petFound?{pteCode} // эндпоинт для работы с поиском, пока оставить пустым.

    @PostMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<PetContactDto> savePetContact (@RequestBody PetContactDto petContactDto) throws Exception {
        System.err.println(petContactDto);
        String petName = petContactDto.getPetName();

        PetContact petContact = petContactMapper.petContactDtoToPetContact(petContactDto);
        petContact.setUniqCode(petContactService.randomUniqueCode());

        //petContact.setPet(new Cat(petName, petContact);
        //petContact.setPet(petName);

        petContactService.persist(petContact);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    // POST api/client/pet/{petId}/qr - добавление информации для создания qrКода. Получает PetContactDto.
    /*PetContactDto {
        String petName;
        String ownerName;
        String address;
        String phone;
    }*/
    //и создает в бд сущность PetContact которая связана с Pet связью OneToOne
}