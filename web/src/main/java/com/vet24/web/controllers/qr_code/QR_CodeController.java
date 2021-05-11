package com.vet24.web.controllers.qr_code;

import com.google.zxing.Result;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.QRCodeGenerator;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.util.Optional;

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
    public ResponseEntity<BufferedImage> createZxingQRCode(@PathVariable("id") Optional<Long> id) throws Exception {
        String barcode = "";
            /*if (id.isPresent()) {
                Pet pet = petService.getPetById(id.get());
            }*/
        /*pet.setId(id);
        pet.setPetName("Оля");
        barcode += pet.getPetName();*/
        //barcode += pet.getPetContact().getOwnerName();
        //barcode += pet.getPetContact().getAddress();
        //barcode += pet.getPetContact().getPhone();
        //barcode += pet.getPetContact().getUniqCode();
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

    @PostMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody String barcode) throws Exception {
        return ResponseEntity.ok(QRCodeGenerator.generateQRCodeImage(barcode));
    }
    // POST api/client/pet/{petId}/qr - добавление информации для создания qrКода. Получает PetContactDto.

    /*@Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }*/
}