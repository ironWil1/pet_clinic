package com.vet24.web.controllers.found;

import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.mappers.pet.PetFoundMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.PetFound;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetFoundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PetFoundController {

    private final PetFoundService petFoundService;
    private final PetContactService petContactService;
    private final PetFoundMapper petFoundMapper;

    public PetFoundController(PetFoundService petFoundService, PetContactService petContactService, PetFoundMapper petFoundMapper) {
        this.petFoundService = petFoundService;
        this.petContactService = petContactService;
        this.petFoundMapper = petFoundMapper;
    }

    @GetMapping(value = "/petFound")
    public ResponseEntity<PetFoundDto> saveOrUpdatePetContact(@RequestBody PetFoundDto petFoundDto,
                                                              @RequestParam(value = "petCode", required = false) String petCode) {
        if (petContactService.isExistByPetCode(petCode)) {
            PetFound petFound = petFoundMapper.petFoundDtoToPetFound(petFoundDto);

            petFoundService.persist(petFound);

            PetContact petContact = petContactService.getByPetCode(petCode);
            String email = petContact.getEmail();

            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
