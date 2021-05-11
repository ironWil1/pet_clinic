package com.vet24.web.controllers.qr_code;

import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.QRCodeGenerator;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/client/pet")
public class QRCodeController {

    @Autowired
    private PetContactService petContactService;

    @Autowired
    private PetService petService;

    /*@GetMapping("/{id}")
    public ResponseEntity<byte[]> getQRCode (@PathVariable long id) {}*/

    /*@GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> createZxingQRCode(@PathVariable("id") String barcode) throws Exception {
        return ResponseEntity.ok(QRCodeGenerator.generateQRCodeImage(barcode));
    }*/

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
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
    //возвращает QR код в котором передана следующая информация:
    // petName
    //ownerName
    //address
    //phone
    //url = /api/petFound?petCode={petCode} - ссылка, на которую надо будет перейти для оповещения владельца. petCode берется из базы.

    // GET /api/petFound?{pteCode} // эндпоинт для работы с поиском, пока оставить пустым.

    /*@PostMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> zxingQRCode(@RequestBody String barcode) throws Exception {
        return ResponseEntity.ok(QRCodeGenerator.generateQRCodeImage(barcode));
    }*/


    // POST api/client/pet/{petId}/qr - добавление информации для создания qrКода. Получает PetContactDto.

    /*@Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }*/
}