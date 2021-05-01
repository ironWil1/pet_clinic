package com.vet24.web.controllers.medicine;

import com.vet24.models.TestClient;
import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.medicine.Mapping;
import com.vet24.service.medicine.MedicineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/manager/medicine")
public class MedicineController {

    @Autowired
    MedicineService medicineService;

    @Autowired
    Mapping mapping;

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getOne(@PathVariable("id") Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        return medicine != null
                ? new ResponseEntity<>(medicine, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        medicineService.deleteMedicine(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(@PathVariable Long id,
                                               @RequestBody MedicineDto medicineDto) {
        Medicine medicine = medicineService.getMedicineById(id);
        medicine.setManufactureName(medicineDto.getManufactureName());
        medicine.setName(medicineDto.getName());
        medicine.setIcon(medicineDto.getIcon());
        medicine.setDescription(medicineDto.getDescription());
        medicineService.editMedicine(medicine);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<Medicine> save(@RequestBody MedicineDto medicineDto) {
        Medicine medicine = mapping.mapToMedicine(medicineDto);
        medicineService.addMedicine(medicine);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}/set-pic")
    public ResponseEntity<String> getPic(@PathVariable("id") Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        return medicine.getIcon() != null
                ? new ResponseEntity<String>(medicine.getIcon(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/set-pic")
    public ResponseEntity<Medicine> savePic(@PathVariable Long id, @RequestBody MedicineDto medicineDto) {
        Medicine medicine = medicineService.getMedicineById(id);
        medicine.setIcon(medicineDto.getIcon());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Medicine>> search(
            @Param("manufactureName") String manufactureName
            , @Param("name") String name
            , @Param("searchtext") String searchtext) {

        List<Medicine> list =  medicineService.search(manufactureName, name, searchtext);
        return new ResponseEntity<List<Medicine>>(list, HttpStatus.OK);
    }



}



