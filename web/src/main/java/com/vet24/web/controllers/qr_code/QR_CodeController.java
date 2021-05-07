package com.vet24.web.controllers.qr_code;

import com.vet24.service.media.ResourceService;
import com.vet24.service.pet.PetContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/pet")
public class QR_CodeController {

    @Autowired
    private PetContactService petContactService;



}