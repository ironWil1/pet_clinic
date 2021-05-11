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
public class QR_CodeController {

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
    public ResponseEntity<byte[]> createZxingQRCode(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        String barcode = "";
        PetContact pet = petContactService.getByKey(id);
        //barcode += pet.getPet().getPetName() + ", ";
        barcode += "Владелец - " + pet.getOwnerName() + ", ";
        barcode += "Адресс - " + pet.getAddress() + ", ";
        barcode += "Телефон - " + pet.getPhone() + ", ";
        barcode += "Уникальный код - " + pet.getUniqCode();

        System.out.println(barcode);

        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(QRCodeGenerator.generateQRCodeImage(barcode));
        outputStream.flush();
        outputStream.close();
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