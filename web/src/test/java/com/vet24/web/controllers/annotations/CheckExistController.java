package com.vet24.web.controllers.annotations;

import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationResponseDto;

import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationResponseMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.models.user.User;
import com.vet24.service.pet.clinicalexamination.ClinicalExaminationService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Profile({"test", "testprod"})
@RequestMapping("/api/doctors")
public class CheckExistController {


    private final ClinicalExaminationService clinicalExaminationService;
    private final ClinicalExaminationResponseMapper clinicalExaminationResponseMapper;


    public CheckExistController(ClinicalExaminationService clinicalExaminationService,
                                ClinicalExaminationResponseMapper clinicalExaminationResponseMapper) {
        this.clinicalExaminationService = clinicalExaminationService;
        this.clinicalExaminationResponseMapper = clinicalExaminationResponseMapper;

    }

    // получение списка по 1 id питомца
    @GetMapping("/exam")
    public ResponseEntity<List<ClinicalExaminationResponseDto>> getExaminationByPetId
            (@CheckExist(entityClass = ClinicalExamination.class) @RequestParam(value = "petId") Long petId) {

        List<ClinicalExamination> clinicalExaminations = clinicalExaminationService.getByPetId(petId);
        return new ResponseEntity<>(clinicalExaminationResponseMapper.toDto(clinicalExaminations), HttpStatus.OK);
    }

    // запрос по двум параметрам, один из них id
    @GetMapping("/exams")
    public ResponseEntity<String> getExaminationByDataAndPetId
            (String data, @CheckExist(entityClass = Pet.class) @RequestParam(value = "petId") Long petId) {
        return new ResponseEntity<>("Произведен запрос по данным " + data +" и Id питомца "+ petId, HttpStatus.OK);
    }

    // запрос по двум id
    @GetMapping("/{doctorId}/exam")
    public ResponseEntity<String> getExaminationByDoctorIdAndPetId
            (@CheckExist(entityClass = User.class)@PathVariable("doctorId") Long id,
             @CheckExist(entityClass = Pet.class) @RequestParam(value = "petId") Long petId) {
        return new ResponseEntity<>( "Произведен запрос по Id доктора " + id +" и id питомца " + petId, HttpStatus.OK);
    }

}
