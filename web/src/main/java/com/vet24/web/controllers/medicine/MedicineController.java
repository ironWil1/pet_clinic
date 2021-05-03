package com.vet24.web.controllers.medicine;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.mappers.MapStructMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.medicine.MedicineService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/manager/medicine")
public class MedicineController {

    private final MedicineService medicineService;
    private final MapStructMapper mapStructMapper;

    public MedicineController(MedicineService medicineService
            , MapStructMapper mapStructMapper) {
        this.medicineService = medicineService;
        this.mapStructMapper = mapStructMapper;
    }


    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getOne(@PathVariable("id") Long id) {
        MedicineDto medicineDto = mapStructMapper
                .medicineToMedicineDto(medicineService.getMedicineById(id));
        return medicineDto != null
                ? new ResponseEntity<>(medicineDto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        if (medicineService.getMedicineById(id) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            medicineService.deleteMedicine(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDto> update(@PathVariable Long id,
                                               @RequestBody MedicineDto medicineDto) {
        Medicine medicine = medicineService.getMedicineById(id);
        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            medicine.setManufactureName(medicineDto.getManufactureName());
            medicine.setName(medicineDto.getName());
            medicine.setIcon(medicineDto.getIcon());
            medicine.setDescription(medicineDto.getDescription());
            medicineService.editMedicine(medicine);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<MedicineDto> save(@RequestBody MedicineDto medicineDto) {
        Medicine medicine = mapStructMapper.medicineDtoToMedicine(medicineDto);
        medicineService.addMedicine(medicine);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @GetMapping("/{id}/set-pic")
//    public ResponseEntity<byte[]> getPic(@PathVariable("id") Long id) {
//        Medicine medicine = medicineService.getMedicineById(id);
//        if (medicine == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } else {
//
//        }
//        return medicine.getIcon() != null
//                ? new ResponseEntity<>(, HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

//    @PostMapping(value = "/{id}/set-pic")
//    public ResponseEntity<Medicine> savePic(@PathVariable Long id, @RequestBody MedicineDto medicineDto) {
//        Medicine medicine = medicineService.getMedicineById(id);
//        medicine.setIcon(medicineDto.getIcon());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @GetMapping("/search")
    public ResponseEntity<List<Medicine>> search(
            @RequestParam(required = false, name = "manufactureName", defaultValue = "") String manufactureName
            ,@RequestParam(required = false, name = "name", defaultValue = "") String name
            , @RequestParam(required = false, name = "searchtext", defaultValue = "") String searchtext) {
        List<Medicine> medicineList = medicineService.searchFull(manufactureName, name, searchtext);
        return new ResponseEntity<>(medicineList, HttpStatus.OK);
    }



}



