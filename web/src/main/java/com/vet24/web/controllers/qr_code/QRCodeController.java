package com.vet24.web.controllers.qr_code;

import com.vet24.models.dto.contact.PetContactDto;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.QRCodeGenerator;
import com.vet24.service.pet.PetContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/pet")
public class QRCodeController {

    @Autowired
    private PetContactService petContactService;

    private PetContactDto petContactDto;

    /*@GetMapping("/{id}")
    public ResponseEntity<byte[]> getQRCode (@PathVariable long id) {}*/

    /*@GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> createZxingQRCode(@PathVariable("id") String barcode) throws Exception {
        return ResponseEntity.ok(QRCodeGenerator.generateQRCodeImage(barcode));
    }*/

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

        //url = /api/petFound?petCode={petCode} - ссылка, на которую надо
        //будет перейти для оповещения владельца. petCode берется из базы.

        System.out.println(barcode);
        return ResponseEntity.ok(QRCodeGenerator.generateQRCodeImage(barcode));
    }

    // GET /api/petFound?{pteCode} // эндпоинт для работы с поиском, пока оставить пустым.

    @PostMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> postZxingQRCode(@RequestParam("petName") String petName,
                                                  @RequestParam("ownerName") String ownerName,
                                                  @RequestParam("address") String address,
                                                  @RequestParam("phone") String phone) throws Exception {
        System.err.println(petName + ", " + ownerName + ", " + address + ", " + phone);
        String barcode = "";
        petContactDto.setPetName(petName);
        petContactDto.setOwnerName(ownerName);
        petContactDto.setAddress(address);
        petContactDto.setPhone(phone);
        //petContactService.persist(petContactDto);
        return ResponseEntity.ok(QRCodeGenerator.generateQRCodeImage(barcode));
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