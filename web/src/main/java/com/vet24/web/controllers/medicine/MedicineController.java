package com.vet24.web.controllers.medicine;

import com.vet24.models.TestClient;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.medicine.MedicineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/manager/medicine")
public class MedicineController {

    @Autowired
    MedicineService medicineService;

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getOne(@PathVariable("id") Long id) {
        final Medicine medicine = medicineService.getMedicineById(id);
        return medicine != null
                ? new ResponseEntity<>(medicine, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}



